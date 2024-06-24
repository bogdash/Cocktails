package com.bogdash.cocktails.presentation.home.filters

import android.content.Context
import android.view.LayoutInflater
import com.bogdash.cocktails.Constants.Filters.DEFAULT_FILTER
import com.bogdash.cocktails.Constants.HomeScreen.ALCOHOLIC
import com.bogdash.cocktails.Constants.HomeScreen.NON_ALCOHOLIC
import com.bogdash.cocktails.databinding.FragmentFiltersBinding
import com.bogdash.cocktails.presentation.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip

class FilterHandler(
    private val context: Context,
    private val homeViewModel: HomeViewModel,
    private val layoutInflater: LayoutInflater
) {
    private var selectedIngredients = mutableListOf<String>()
    private var typeAlcoholFilter: String = DEFAULT_FILTER
    private lateinit var binding: FragmentFiltersBinding

    fun showBottomSheetDialog() {
        binding = FragmentFiltersBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(context)
        setupChips()
        initListeners(dialog)

        restoreFilters()

        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun initListeners(dialog: BottomSheetDialog) {
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnReset.setOnClickListener {
            with(binding) {
                chipAlcoholic.isChecked = true
                chipGroupIngredients.clearCheck()
            }
            homeViewModel.setDefaultFilterType()
        }

        binding.btnApply.setOnClickListener {
            applyFilters()
            dialog.dismiss()
        }
    }

    private fun setupChips() {
        setupIngredientChips()
        setupAlcoholChips()
    }

    private fun setupIngredientChips() {
        binding.chipGroupIngredients.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.chipGroupAlcohol.clearCheck()
            }

            selectedIngredients.clear()
            checkedIds.forEach { id ->
                val chip = group.findViewById<Chip>(id)
                if (chip != null) {
                    selectedIngredients.add(chip.text.toString())
                }
            }
        }
    }

    private fun setupAlcoholChips() {
        binding.chipGroupAlcohol.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.chipGroupIngredients.clearCheck()
            }
        }
    }

    private fun applyAlcoholFilter() {
        when (binding.chipGroupAlcohol.checkedChipId) {
            binding.chipAlcoholic.id -> typeAlcoholFilter = ALCOHOLIC
            binding.chipNonAlcoholic.id -> typeAlcoholFilter = NON_ALCOHOLIC
        }
        homeViewModel.setAlcoholicFilterType(typeAlcoholFilter)
    }

    private fun applyIngredientsFilter() {
        homeViewModel.setIngredientsFilter(selectedIngredients)
    }

    private fun applyFilters() {
        if (binding.chipGroupAlcohol.checkedChipIds.isNotEmpty())
            applyAlcoholFilter()

        if (binding.chipGroupIngredients.checkedChipIds.isNotEmpty())
            applyIngredientsFilter()
    }

    private fun restoreFilters() {
        if (homeViewModel.isAlcoholFilterApplied.value == true)
            homeViewModel.alcoholicFilterType.value?.let { filterType ->
                when (filterType) {
                    ALCOHOLIC -> binding.chipAlcoholic.isChecked = true
                    NON_ALCOHOLIC -> binding.chipNonAlcoholic.isChecked = true
                }
            }
        else {
            homeViewModel.ingredientsFilterType.value?.let { ingredients ->
                val ingredientsCopy = ingredients.toList()
                ingredientsCopy.forEach { ingredient ->
                    val chip = binding.chipGroupIngredients.findViewWithTag<Chip>(ingredient)
                    chip?.isChecked = true
                }
            }
        }
    }
}
