package com.plantguide.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.plantguide.databinding.ActivityFavoritesBinding
import com.plantguide.ui.PlantViewModel
import com.plantguide.ui.detail.PlantDetailActivity
import com.plantguide.ui.home.PlantAdapter

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: PlantViewModel
    private lateinit var adapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(com.plantguide.R.string.nav_favorites)

        viewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        adapter = PlantAdapter { plant ->
            startActivity(
                Intent(this, PlantDetailActivity::class.java).apply {
                    putExtra(PlantDetailActivity.EXTRA_PLANT_ID, plant.id)
                }
            )
        }

        binding.rvFavorites.layoutManager = GridLayoutManager(this, 2)
        binding.rvFavorites.adapter = adapter

        viewModel.favoritePlants.observe(this) { favorites ->
            adapter.submitList(favorites)
            binding.tvEmptyFavorites.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
            binding.rvFavorites.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
