package com.bogdash.cocktails.presentation.home.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bogdash.cocktails.Constants
import com.bogdash.cocktails.databinding.FragmentFiltersBinding
import com.bogdash.cocktails.presentation.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiltersFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFiltersBinding

    private var selectedIngredients = mutableListOf<String>()
    private var typeAlcoholFilter: String = Constants.Filters.DEFAULT_FILTER

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChips()
        initListeners()
        observeFilterApplied()
    }

    private fun initListeners() {
        with(binding) {
            btnClose.setOnClickListener {
                dismiss()
            }

            btnReset.setOnClickListener {
                chipAlcoholic.isChecked = true
                chipGroupIngredients.clearCheck()
                homeViewModel.setDefaultFilterType()
            }

            btnApply.setOnClickListener {
                applyFilters()
                dismiss()
            }
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
            binding.chipAlcoholic.id -> typeAlcoholFilter = Constants.HomeScreen.ALCOHOLIC
            binding.chipNonAlcoholic.id -> typeAlcoholFilter = Constants.HomeScreen.NON_ALCOHOLIC
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

    private fun observeFilterApplied() {
        homeViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (homeViewModel.isAlcoholFilterApplied.value == true)
                homeViewModel.alcoholicFilterType.value?.let { filterType ->
                    when (filterType) {
                        Constants.HomeScreen.ALCOHOLIC -> binding.chipAlcoholic.isChecked = true
                        Constants.HomeScreen.NON_ALCOHOLIC -> binding.chipNonAlcoholic.isChecked =
                            true
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

    companion object {
        @JvmStatic
        fun newInstance() = FiltersFragment()
    }
}
