package com.plantguide.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModelProvider
import androidx.core.content.ContextCompat
import com.plantguide.data.entity.Plant
import com.plantguide.ui.PlantViewModel
import com.plantguide.R
import com.plantguide.databinding.ActivityPlantScanBinding
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlantScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantScanBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var plantViewModel: PlantViewModel

    private var imageCapture: ImageCapture? = null
    private var isAnalyzing = false
    private var lastScannedPlant: ScanResult? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else {
                Toast.makeText(this, getString(R.string.scan_camera_permission_denied), Toast.LENGTH_LONG).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.scan_title)

        cameraExecutor = Executors.newSingleThreadExecutor()
        plantViewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        binding.btnCapture.setOnClickListener { captureAndAnalyze() }
        binding.btnScanAgain.setOnClickListener { resetOverlay() }
        binding.btnAddScannedPlant.setOnClickListener { saveScannedPlant() }

        if (hasCameraPermission()) startCamera()
        else requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                showError(getString(R.string.scan_camera_error))
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureAndAnalyze() {
        if (isAnalyzing) return
        val capture = imageCapture ?: return

        isAnalyzing = true
        showLoading(true)

        capture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val base64 = imageToCaptureBase64(image)
                    image.close()
                    if (base64 != null) sendToGemini(base64)
                    else showError(getString(R.string.scan_capture_error))
                }

                override fun onError(exception: ImageCaptureException) {
                    isAnalyzing = false
                    showLoading(false)
                    showError(getString(R.string.scan_capture_error))
                }
            }
        )
    }

    private fun imageToCaptureBase64(image: ImageProxy): String? {
        return try {
            val bitmap: Bitmap = when (image.format) {
                ImageFormat.YUV_420_888 -> yuvToBitmap(image)
                else -> {
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
            } ?: return null

            val out = ByteArrayOutputStream()
            val scaled = scaleBitmap(bitmap, 800)
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
            Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    private fun yuvToBitmap(image: ImageProxy): Bitmap? {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 90, out)
        val bytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun scaleBitmap(src: Bitmap, maxSide: Int): Bitmap {
        val ratio = maxSide.toFloat() / maxOf(src.width, src.height)
        if (ratio >= 1f) return src
        return Bitmap.createScaledBitmap(src, (src.width * ratio).toInt(), (src.height * ratio).toInt(), true)
    }

    // ---------------------------------------------------------------
    //  Gemini Vision API  — envia a foto e recebe JSON com os dados
    // ---------------------------------------------------------------
    private fun sendToGemini(base64Image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = getString(R.string.gemini_api_key)
                val endpoint =
                    "https://generativelanguage.googleapis.com/v1beta/models/" +
                            "gemini-2.0-flash:generateContent?key=$apiKey"

                val prompt = """
                    Analise esta imagem e identifique a planta.
                    Responda SOMENTE com JSON válido, sem texto extra, no formato:
                    {
                      "found": true,
                      "commonName": "Nome popular em português",
                      "scientificName": "Nome científico",
                      "family": "Família botânica",
                      "origin": "País ou região de origem",
                      "lightLevel": "Nível de luz (ex: Pleno sol, Meia sombra, Sombra)",
                      "wateringFrequency": "Frequência de rega (ex: 2x por semana)",
                      "idealEnvironment": "Ambiente ideal (ex: Interno, Externo, Ambos)",
                      "basicCare": "Dicas de cuidado básico (máx 3 frases)",
                      "isToxicForPets": true,
                      "toxicityNote": "Breve explicação de toxicidade ou segurança para animais",
                      "curiosity": "Uma curiosidade interessante sobre a planta (1 frase)"
                    }
                    Se não houver planta visível ou não for possível identificar, responda:
                    {"found": false}
                """.trimIndent()

                val requestBody = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply { put("text", prompt) })
                                put(JSONObject().apply {
                                    put("inline_data", JSONObject().apply {
                                        put("mime_type", "image/jpeg")
                                        put("data", base64Image)
                                    })
                                })
                            })
                        })
                    })
                    put("generationConfig", JSONObject().apply {
                        put("temperature", 0.2)
                        put("maxOutputTokens", 800)
                    })
                }

                val url = URL(endpoint)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 20_000
                conn.readTimeout = 20_000
                conn.outputStream.use { it.write(requestBody.toString().toByteArray()) }

                val responseCode = conn.responseCode
                val responseText = if (responseCode == 200) {
                    conn.inputStream.bufferedReader().readText()
                } else {
                    conn.errorStream?.bufferedReader()?.readText() ?: "Erro $responseCode"
                }
                conn.disconnect()

                withContext(Dispatchers.Main) {
                    if (responseCode == 200) {
                        parseAndShowResult(responseText)
                    } else {
                        val detail = when (responseCode) {
                            400 -> "Chave inválida ou requisição malformada (400)"
                            401 -> "Chave API não autorizada (401)"
                            403 -> "Acesso negado — verifique se a API Gemini está ativada no Google AI Studio (403)"
                            404 -> "Modelo não encontrado (404)"
                            429 -> "Limite de requisições atingido — tente novamente em instantes (429)"
                            500, 503 -> "Servidor Google indisponível — tente novamente ($responseCode)"
                            else -> "Erro $responseCode: ${responseText.take(120)}"
                        }
                        showError(detail)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { showError(getString(R.string.scan_network_error)) }
            }
        }
    }

    private fun parseAndShowResult(rawResponse: String) {
        try {
            val geminiJson = JSONObject(rawResponse)
            val text = geminiJson
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            val jsonStr = text.replace("```json", "").replace("```", "").trim()
            val result = JSONObject(jsonStr)

            if (!result.optBoolean("found", false)) {
                showNotFound()
                return
            }

            showResult(
                ScanResult(
                    commonName     = result.optString("commonName", "-"),
                    scientificName = result.optString("scientificName", "-"),
                    family         = result.optString("family", "-"),
                    origin         = result.optString("origin", "-"),
                    lightLevel     = result.optString("lightLevel", "-"),
                    watering       = result.optString("wateringFrequency", "-"),
                    idealEnvironment = result.optString("idealEnvironment", "-"),
                    basicCare      = result.optString("basicCare", "-"),
                    isToxic        = result.optBoolean("isToxicForPets", false),
                    toxicityNote   = result.optString("toxicityNote", ""),
                    curiosity      = result.optString("curiosity", "")
                )
            )
        } catch (e: Exception) {
            showError(getString(R.string.scan_parse_error))
        }
    }

    // ---------------------------------------------------------------
    //  UI helpers
    // ---------------------------------------------------------------
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.tvAnalyzing.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnCapture.isEnabled = !show
    }

    private fun showResult(plant: ScanResult) {
        lastScannedPlant = plant
        showLoading(false)
        isAnalyzing = false
        binding.cardResult.visibility = View.VISIBLE
        binding.btnCapture.visibility = View.GONE
        binding.btnScanAgain.visibility = View.VISIBLE
        binding.btnAddScannedPlant.visibility = View.VISIBLE
        binding.btnAddScannedPlant.isEnabled = true
        binding.btnAddScannedPlant.text = getString(R.string.scan_add_plant)

        binding.tvResultName.text           = plant.commonName
        binding.tvResultScientific.text     = plant.scientificName
        binding.tvResultFamily.text         = plant.family
        binding.tvResultOrigin.text         = plant.origin
        binding.tvResultLight.text          = plant.lightLevel
        binding.tvResultWater.text          = plant.watering
        binding.tvResultEnvironment.text    = plant.idealEnvironment
        binding.tvResultCare.text           = plant.basicCare
        binding.tvResultToxicity.text       = plant.toxicityNote
        binding.tvResultCuriosity.text      = plant.curiosity

        if (plant.isToxic) {
            binding.chipToxicity.text = getString(R.string.toxic_for_pets)
            binding.chipToxicity.setChipBackgroundColorResource(R.color.toxic_background)
            binding.chipToxicity.setTextColor(getColor(R.color.toxic_red))
            binding.ivToxicityIcon.setImageResource(R.drawable.ic_toxic)
        } else {
            binding.chipToxicity.text = getString(R.string.safe_for_pets)
            binding.chipToxicity.setChipBackgroundColorResource(R.color.safe_background)
            binding.chipToxicity.setTextColor(getColor(R.color.safe_green))
            binding.ivToxicityIcon.setImageResource(R.drawable.ic_safe)
        }
    }

    private fun showNotFound() {
        showLoading(false)
        isAnalyzing = false
        Toast.makeText(this, getString(R.string.scan_not_found), Toast.LENGTH_LONG).show()
    }

    private fun showError(message: String) {
        showLoading(false)
        isAnalyzing = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun resetOverlay() {
        isAnalyzing = false
        lastScannedPlant = null
        binding.cardResult.visibility = View.GONE
        binding.btnCapture.visibility = View.VISIBLE
        binding.btnScanAgain.visibility = View.GONE
        binding.btnAddScannedPlant.visibility = View.GONE
        binding.btnAddScannedPlant.isEnabled = true
        binding.btnAddScannedPlant.text = getString(R.string.scan_add_plant)
    }

    private fun saveScannedPlant() {
        val scanned = lastScannedPlant ?: return

        val plant = Plant(
            name = scanned.commonName.ifBlank { "Planta escaneada" },
            scientificName = scanned.scientificName.ifBlank { "-" },
            imageResName = "ic_plant_placeholder",
            imageUrl = "",
            lightLevel = scanned.lightLevel.ifBlank { "-" },
            wateringFrequency = scanned.watering.ifBlank { "-" },
            idealEnvironment = scanned.idealEnvironment.ifBlank { "-" },
            basicCare = scanned.basicCare.ifBlank { "-" },
            isToxicForPets = scanned.isToxic,
            category = getString(R.string.scan_unknown_category),
            isFavorite = false
        )

        plantViewModel.insertPlant(plant)

        Toast.makeText(this, getString(R.string.scan_plant_saved), Toast.LENGTH_SHORT).show()
        binding.btnAddScannedPlant.isEnabled = false
        binding.btnAddScannedPlant.text = getString(R.string.scan_added)
    }

    data class ScanResult(
        val commonName: String,
        val scientificName: String,
        val family: String,
        val origin: String,
        val lightLevel: String,
        val watering: String,
        val idealEnvironment: String,
        val basicCare: String,
        val isToxic: Boolean,
        val toxicityNote: String,
        val curiosity: String
    )
}
