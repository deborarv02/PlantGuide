package com.plantguide.data

import androidx.lifecycle.LiveData
import com.plantguide.data.dao.PlantDao
import com.plantguide.data.entity.Plant

class PlantRepository(private val plantDao: PlantDao) {

    val allPlants: LiveData<List<Plant>> = plantDao.getAllPlants()
    val favoritePlants: LiveData<List<Plant>> = plantDao.getFavoritePlants()

    fun getPlantById(id: Int): LiveData<Plant> = plantDao.getPlantById(id)

    fun searchPlants(query: String): LiveData<List<Plant>> = plantDao.searchPlants(query)

    fun getPlantsByCategory(category: String): LiveData<List<Plant>> =
        plantDao.getPlantsByCategory(category)

    suspend fun insertPlant(plant: Plant) = plantDao.insertPlant(plant)

    suspend fun updatePlant(plant: Plant) = plantDao.updatePlant(plant)

    suspend fun deletePlant(plant: Plant) = plantDao.deletePlant(plant)

    suspend fun toggleFavorite(plant: Plant) {
        plantDao.updateFavoriteStatus(plant.id, !plant.isFavorite)
    }

    suspend fun getPlantCount(): Int = plantDao.getPlantCount()
}
