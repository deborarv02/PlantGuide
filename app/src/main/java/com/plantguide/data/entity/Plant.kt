package com.plantguide.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val scientificName: String,
    val imageResName: String,
    val imageUrl: String = "",
    val lightLevel: String,
    val wateringFrequency: String,
    val idealEnvironment: String,
    val basicCare: String,
    val isToxicForPets: Boolean,
    val category: String,
    val isFavorite: Boolean = false
)
