package com.plantguide.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.plantguide.data.entity.Plant

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY name ASC")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlantById(id: Int): LiveData<Plant>

    @Query("SELECT * FROM plants WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE name LIKE '%' || :query || '%' OR scientificName LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchPlants(query: String): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE category = :category ORDER BY name ASC")
    fun getPlantsByCategory(category: String): LiveData<List<Plant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Plant>)

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)

    @Query("UPDATE plants SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM plants")
    suspend fun getPlantCount(): Int
}
