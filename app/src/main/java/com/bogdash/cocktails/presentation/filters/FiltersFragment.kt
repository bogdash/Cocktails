package com.bogdash.cocktails.presentation.filters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bogdash.cocktails.Constants.HomeScreen.ALCOHOLIC
import com.bogdash.cocktails.Constants.HomeScreen.NON_ALCOHOLIC
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentFiltersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private lateinit var binding: FragmentFiltersBinding
    private val filtersViewModel: FiltersViewModel by viewModels()

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
        }

        binding.btnApply.setOnClickListener {
            val typeAlcoholFilter: String
            when(binding.chipGroupAlcohol.checkedChipId) {
                0 -> typeAlcoholFilter = ALCOHOLIC
                1 -> typeAlcoholFilter = NON_ALCOHOLIC
                else -> typeAlcoholFilter = ALCOHOLIC
            }
            filtersViewModel.setFilterType(typeAlcoholFilter)
            filtersViewModel.applyFilters()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FiltersFragment()
    }
}
