package com.plantguide.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.plantguide.R
import com.plantguide.data.entity.Plant
import com.plantguide.databinding.ActivityPlantDetailBinding
import com.plantguide.ui.PlantViewModel
import com.plantguide.ui.addedit.AddEditPlantActivity

class PlantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding
    private lateinit var viewModel: PlantViewModel
    private var currentPlant: Plant? = null

    companion object {
        const val EXTRA_PLANT_ID = "extra_plant_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        val plantId = intent.getIntExtra(EXTRA_PLANT_ID, -1)
        if (plantId == -1) { finish(); return }

        viewModel.getPlantById(plantId).observe(this) { plant ->
            plant ?: return@observe
            currentPlant = plant

            supportActionBar?.title = plant.name

            val resId = resources.getIdentifier(plant.imageResName, "drawable", packageName)
            if (resId != 0) {
                binding.ivPlantDetail.load(resId) {
                    crossfade(true)
                    placeholder(R.drawable.ic_plant_placeholder)
                    error(R.drawable.ic_plant_placeholder)
                }
            } else if (plant.imageUrl.isNotEmpty()) {
                binding.ivPlantDetail.load(plant.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_plant_placeholder)
                    error(R.drawable.ic_plant_placeholder)
                }
            } else {
                binding.ivPlantDetail.setImageResource(R.drawable.ic_plant_placeholder)
            }

            binding.tvPlantName.text = plant.name
            binding.tvScientificName.text = plant.scientificName
            binding.tvCategory.text = plant.category
            binding.tvLightValue.text = plant.lightLevel
            binding.tvWaterValue.text = plant.wateringFrequency
            binding.tvEnvironmentValue.text = plant.idealEnvironment
            binding.tvCareValue.text = plant.basicCare

            if (plant.isToxicForPets) {
                binding.tvPetStatus.text = getString(R.string.toxic_for_pets)
                binding.tvPetStatus.setTextColor(getColor(R.color.toxic_red))
                binding.cardPetStatus.setCardBackgroundColor(getColor(R.color.toxic_background))
                binding.ivPetStatus.setImageResource(R.drawable.ic_toxic)
            } else {
                binding.tvPetStatus.text = getString(R.string.safe_for_pets)
                binding.tvPetStatus.setTextColor(getColor(R.color.safe_green))
                binding.cardPetStatus.setCardBackgroundColor(getColor(R.color.safe_background))
                binding.ivPetStatus.setImageResource(R.drawable.ic_safe)
            }

            updateFavoriteButton(plant.isFavorite)
            binding.fabFavorite.setOnClickListener { viewModel.toggleFavorite(plant) }

            // Invalida menu para mostrar opções corretas após carregar a planta
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_plant_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_edit -> {
                currentPlant?.let { plant ->
                    val intent = Intent(this, AddEditPlantActivity::class.java).apply {
                        putExtra(AddEditPlantActivity.EXTRA_PLANT_ID, plant.id)
                    }
                    startActivity(intent)
                }
                true
            }
            R.id.action_delete -> {
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDelete() {
        val plant = currentPlant ?: return
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_delete_title))
            .setMessage(getString(R.string.dialog_delete_message, plant.name))
            .setPositiveButton(getString(R.string.dialog_delete_confirm)) { _, _ ->
                viewModel.deletePlant(plant)
                finish()
            }
            .setNegativeButton(getString(R.string.dialog_delete_cancel), null)
            .show()
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
        )
    }
}
