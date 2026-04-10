package com.plantguide.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.plantguide.R
import com.plantguide.databinding.FragmentPlantListBinding
import com.plantguide.ui.PlantViewModel
import com.plantguide.ui.addedit.AddEditPlantActivity
import com.plantguide.ui.detail.PlantDetailActivity

class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlantViewModel
    private lateinit var adapter: PlantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PlantViewModel::class.java]

        setupRecyclerView()
        setupSearch()
        setupObservers()

        // FAB: abrir tela de adicionar nova planta
        binding.fabAddPlant.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditPlantActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = PlantAdapter { plant ->
            startActivity(
                Intent(requireContext(), PlantDetailActivity::class.java).apply {
                    putExtra(PlantDetailActivity.EXTRA_PLANT_ID, plant.id)
                }
            )
        }
        binding.rvPlants.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlants.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }

    private fun setupObservers() {
        viewModel.filteredPlants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
            binding.tvEmpty.visibility = if (plants.isEmpty()) View.VISIBLE else View.GONE
            binding.rvPlants.visibility = if (plants.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.allPlants.observe(viewLifecycleOwner) { plants ->
            val categories = plants.map { it.category }.distinct().sorted()
            setupCategoryChips(categories)
        }
    }

    private fun setupCategoryChips(categories: List<String>) {
        if (binding.chipGroupCategory.childCount > 1) return

        for (category in categories) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                setChipBackgroundColorResource(R.color.chip_background_selector)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
            }
            binding.chipGroupCategory.addView(chip)
        }

        binding.chipGroupCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty() || checkedIds.contains(R.id.chipAll)) {
                viewModel.setSelectedCategory(null)
            } else {
                val selectedChip = group.findViewById<Chip>(checkedIds.first())
                viewModel.setSelectedCategory(selectedChip?.text?.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
