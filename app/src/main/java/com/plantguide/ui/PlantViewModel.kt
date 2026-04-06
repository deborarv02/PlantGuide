package com.plantguide.ui

import android.app.Application
import androidx.lifecycle.*
import com.plantguide.data.PlantRepository
import com.plantguide.data.database.PlantDatabase
import com.plantguide.data.entity.Plant
import kotlinx.coroutines.launch

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    val allPlants: LiveData<List<Plant>>
    val favoritePlants: LiveData<List<Plant>>

    init {
        val dao = PlantDatabase.getDatabase(application).plantDao()
        repository = PlantRepository(dao)
        allPlants = repository.allPlants
        favoritePlants = repository.favoritePlants
    }

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _selectedCategory = MutableLiveData<String?>(null)
    val selectedCategory: LiveData<String?> = _selectedCategory

    val filteredPlants: LiveData<List<Plant>> = MediatorLiveData<List<Plant>>().apply {
        val mediator = this
        var plants: List<Plant> = emptyList()
        var query = ""
        var category: String? = null

        mediator.addSource(allPlants) { list ->
            plants = list ?: emptyList()
            mediator.value = applyFilters(plants, query, category)
        }
        mediator.addSource(_searchQuery) { q ->
            query = q ?: ""
            mediator.value = applyFilters(plants, query, category)
        }
        mediator.addSource(_selectedCategory) { cat ->
            category = cat
            mediator.value = applyFilters(plants, query, category)
        }
    }

    private fun applyFilters(
        plants: List<Plant>,
        query: String,
        category: String?
    ): List<Plant> {
        var result = plants
        if (query.isNotBlank()) {
            result = result.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.scientificName.contains(query, ignoreCase = true)
            }
        }
        if (!category.isNullOrBlank()) {
            result = result.filter { it.category == category }
        }
        return result
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedCategory(category: String?) { _selectedCategory.value = category }
    fun getPlantById(id: Int): LiveData<Plant> = repository.getPlantById(id)
    fun toggleFavorite(plant: Plant) = viewModelScope.launch { repository.toggleFavorite(plant) }
    fun updatePlant(plant: Plant) = viewModelScope.launch { repository.updatePlant(plant) }
    fun deletePlant(plant: Plant) = viewModelScope.launch { repository.deletePlant(plant) }
    fun getCategories(): List<String> =
        allPlants.value?.map { it.category }?.distinct()?.sorted() ?: emptyList()
}
