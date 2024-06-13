package com.bogdash.cocktails.presentation.filters

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.forEach
import androidx.core.view.isNotEmpty
import com.bogdash.cocktails.Constants.Filters.DEFAULT_FILTER
import com.bogdash.cocktails.Constants.HomeScreen.ALCOHOLIC
import com.bogdash.cocktails.Constants.HomeScreen.NON_ALCOHOLIC
import com.bogdash.cocktails.databinding.FragmentFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip

class FilterHandler(
    private val context: Context,
    private val filtersViewModel: FiltersViewModel,
    private val layoutInflater: LayoutInflater
) {
    private val selectedIngredients = mutableListOf<String>()
    private lateinit var binding: FragmentFiltersBinding

    fun showBottomSheetDialog() {
        binding = FragmentFiltersBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(context)
        setupIngredientChips()
        setupAlcoholChips()
        initListeners(dialog)

        restoreFilters()

        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun initListeners(dialog: BottomSheetDialog) {
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnReset.setOnClickListener  {
            with(binding) {
                chipAlcoholic.isChecked = true
                chipGroupIngredients.clearCheck()
            }
            filtersViewModel.setDefaultFilterType()
        }

        binding.btnApply.setOnClickListener {
            applyFilters()
            dialog.dismiss()
        }
    }

    private fun setupIngredientChips() {
        binding.chipGroupIngredients.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
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
        var typeAlcoholFilter: String = DEFAULT_FILTER
        when (binding.chipGroupAlcohol.checkedChipId) {
            binding.chipAlcoholic.id -> typeAlcoholFilter = ALCOHOLIC
            binding.chipNonAlcoholic.id -> typeAlcoholFilter = NON_ALCOHOLIC
        }
        filtersViewModel.setAlcoholicFilterType(typeAlcoholFilter)
    }

    private fun applyIngredientsFilter() {
        binding.chipGroupIngredients.forEach { chip ->
            if (chip is Chip && chip.isChecked) {
                selectedIngredients.add(chip.text.toString())
            }
        }
        filtersViewModel.setIngredientsFilter(selectedIngredients)
    }

    private fun applyFilters() {
        if (binding.chipGroupAlcohol.isNotEmpty())
            applyAlcoholFilter()

        if (binding.chipGroupIngredients.isNotEmpty())
            applyIngredientsFilter()
    }

    private fun restoreFilters() {
        filtersViewModel.alcoholicFilterType.value?.let { filterType ->
            when (filterType) {
                ALCOHOLIC -> binding.chipAlcoholic.isChecked = true
                NON_ALCOHOLIC -> binding.chipNonAlcoholic.isChecked = true
            }
        }
        filtersViewModel.ingredientsFilterType.value?.let { ingredients ->
            ingredients.forEach { ingredient ->
                val chip = binding.chipGroupIngredients.findViewWithTag<Chip>(ingredient)
                chip?.isChecked = true
            }
        }
    }
}
