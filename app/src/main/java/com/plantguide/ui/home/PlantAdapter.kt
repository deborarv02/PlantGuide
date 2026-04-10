package com.plantguide.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.plantguide.R
import com.plantguide.data.entity.Plant
import com.plantguide.databinding.ItemPlantBinding

class PlantAdapter(
    private val onItemClick: (Plant) -> Unit
) : ListAdapter<Plant, PlantAdapter.PlantViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlantViewHolder(private val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plant: Plant) {
            binding.tvPlantName.text = plant.name
            binding.tvScientificName.text = plant.scientificName
            binding.tvCategory.text = plant.category

            binding.ivPetIcon.setImageResource(
                if (plant.isToxicForPets) R.drawable.ic_toxic else R.drawable.ic_safe
            )
            binding.ivPetIcon.contentDescription =
                if (plant.isToxicForPets) "Tóxica para pets" else "Segura para pets"

            val resId = binding.root.context.resources.getIdentifier(
                plant.imageResName, "drawable", binding.root.context.packageName
            )

            if (resId != 0) {
                binding.ivPlant.load(resId) {
                    crossfade(true)
                    placeholder(R.drawable.ic_plant_placeholder)
                    error(R.drawable.ic_plant_placeholder)
                    transformations(RoundedCornersTransformation(16f))
                }
            } else if (plant.imageUrl.isNotEmpty()) {
                binding.ivPlant.load(plant.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_plant_placeholder)
                    error(R.drawable.ic_plant_placeholder)
                    transformations(RoundedCornersTransformation(16f))
                }
            } else {
                binding.ivPlant.setImageResource(R.drawable.ic_plant_placeholder)
            }

            binding.ivFavorite.setImageResource(
                if (plant.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
            )

            binding.root.setOnClickListener { onItemClick(plant) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Plant, newItem: Plant) = oldItem == newItem
    }
}
