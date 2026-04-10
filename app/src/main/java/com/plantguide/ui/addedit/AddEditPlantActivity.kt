package com.plantguide.ui.addedit

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.plantguide.R
import com.plantguide.data.entity.Plant
import com.plantguide.databinding.ActivityAddEditPlantBinding
import com.plantguide.ui.PlantViewModel

class AddEditPlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPlantBinding
    private lateinit var viewModel: PlantViewModel

    private var editingPlantId: Int = -1
    private var currentPlant: Plant? = null

    companion object {
        const val EXTRA_PLANT_ID = "extra_plant_id_edit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        editingPlantId = intent.getIntExtra(EXTRA_PLANT_ID, -1)
        val isEditing = editingPlantId != -1

        supportActionBar?.title = if (isEditing) getString(R.string.title_edit_plant)
                                   else getString(R.string.title_add_plant)

        if (isEditing) {
            viewModel.getPlantById(editingPlantId).observe(this) { plant ->
                plant ?: return@observe
                if (currentPlant == null) {
                    currentPlant = plant
                    populateFields(plant)
                }
            }
        }

        binding.btnSave.setOnClickListener { savePlant() }
    }

    private fun populateFields(plant: Plant) {
        binding.etName.setText(plant.name)
        binding.etScientificName.setText(plant.scientificName)
        binding.etCategory.setText(plant.category)
        binding.etLightLevel.setText(plant.lightLevel)
        binding.etWateringFrequency.setText(plant.wateringFrequency)
        binding.etIdealEnvironment.setText(plant.idealEnvironment)
        binding.etBasicCare.setText(plant.basicCare)
        binding.switchToxic.isChecked = plant.isToxicForPets
        binding.etImageUrl.setText(plant.imageUrl)
    }

    private fun savePlant() {
        val name = binding.etName.text.toString().trim()
        val scientificName = binding.etScientificName.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val lightLevel = binding.etLightLevel.text.toString().trim()
        val wateringFrequency = binding.etWateringFrequency.text.toString().trim()
        val idealEnvironment = binding.etIdealEnvironment.text.toString().trim()
        val basicCare = binding.etBasicCare.text.toString().trim()
        val isToxic = binding.switchToxic.isChecked
        val imageUrl = binding.etImageUrl.text.toString().trim()

        if (name.isBlank() || scientificName.isBlank() || category.isBlank()) {
            Toast.makeText(this, getString(R.string.error_required_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val isEditing = editingPlantId != -1

        val plant = Plant(
            id = if (isEditing) editingPlantId else 0,
            name = name,
            scientificName = scientificName,
            imageResName = currentPlant?.imageResName ?: "ic_plant_placeholder",
            imageUrl = imageUrl,
            lightLevel = lightLevel.ifBlank { "-" },
            wateringFrequency = wateringFrequency.ifBlank { "-" },
            idealEnvironment = idealEnvironment.ifBlank { "-" },
            basicCare = basicCare.ifBlank { "-" },
            isToxicForPets = isToxic,
            category = category,
            isFavorite = currentPlant?.isFavorite ?: false
        )

        if (isEditing) {
            viewModel.updatePlant(plant)
            Toast.makeText(this, getString(R.string.plant_updated), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertPlant(plant)
            Toast.makeText(this, getString(R.string.plant_added), Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
