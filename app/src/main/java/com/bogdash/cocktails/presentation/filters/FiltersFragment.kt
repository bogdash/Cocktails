package com.bogdash.cocktails.presentation.filters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.bogdash.cocktails.Constants.Filters.DEFAULT_FILTER
import com.bogdash.cocktails.Constants.HomeScreen.ALCOHOLIC
import com.bogdash.cocktails.Constants.HomeScreen.NON_ALCOHOLIC
import com.bogdash.cocktails.databinding.FragmentFiltersBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private lateinit var binding: FragmentFiltersBinding
    private val filtersViewModel: FiltersViewModel by viewModels()
    private val selectedIngredients = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setupIngredientChips()
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.btnReset.setOnClickListener  {
            with(binding) {
                chipAlcoholic.isChecked
                chipGroupIngredients.clearCheck()
            }
            filtersViewModel.setDefaultFilterType()
        }

        binding.btnApply.setOnClickListener {
            applyAlcoholFilter()
            applyIngredientsFilter()
        }
    }

    private fun applyAlcoholFilter() {
        var typeAlcoholFilter: String = DEFAULT_FILTER
        when(binding.chipGroupAlcohol.checkedChipId) {
            0 -> typeAlcoholFilter = ALCOHOLIC
            1 -> typeAlcoholFilter = NON_ALCOHOLIC
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

    private fun setupIngredientChips() {
        binding.chipGroupIngredients.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedIngredients.clear()
            checkedIds.forEach { id ->
                val chip = group.findViewById<Chip>(id)
                if (chip != null) {
                    selectedIngredients.add(chip.text.toString())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FiltersFragment()
    }
}
