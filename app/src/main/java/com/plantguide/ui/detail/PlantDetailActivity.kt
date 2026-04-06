package com.plantguide.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.plantguide.R
import com.plantguide.databinding.ActivityPlantDetailBinding
import com.plantguide.ui.PlantViewModel

class PlantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding
    private lateinit var viewModel: PlantViewModel

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

            supportActionBar?.title = plant.name

            // Carrega imagem real da internet usando Coil
            if (plant.imageUrl.isNotEmpty()) {
                binding.ivPlantDetail.load(plant.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_plant_placeholder)
                    error(R.drawable.ic_plant_placeholder)
                }
            } else {
                val resId = resources.getIdentifier(plant.imageResName, "drawable", packageName)
                binding.ivPlantDetail.setImageResource(
                    if (resId != 0) resId else R.drawable.ic_plant_placeholder
                )
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
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
