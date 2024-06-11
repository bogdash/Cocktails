package com.bogdash.cocktails.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailBinding
import com.bogdash.cocktails.presentation.detail.instructions.InstructionsFragment
import com.bogdash.cocktails.presentation.detail.ingredients.IngredientsFragment
import com.bogdash.cocktails.presentation.detail.models.mappers.toParcelable
import com.bogdash.domain.models.Cocktails
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var drinkId: String? = null
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            drinkId = it.getString(ARG_DRINK_ID)
        }

        initListeners()
        observeViewModel()
        loadCocktailDetails()
    }

    private fun initListeners() {
        initTabLayout()

        with(binding.contentLayout) {
            btnBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            btnFavorite.setOnClickListener {
                detailViewModel.toggleFavorite()
            }
        }
    }

    private fun initTabLayout() {
        binding.contentLayout.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                detailViewModel.setSelectedTab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
            observeResultCocktails()
            observeFavoriteState()
            observeSelectedTab()
            observeUiMessageChannel()
    }

    private fun observeResultCocktails() {
        detailViewModel.resultCocktails.observe(viewLifecycleOwner) {
            it?.let {
                updateUI(it)
                detailViewModel.setSelectedTab(TAB_LAYOUT_LEFT)
            }
        }
    }

    private fun observeFavoriteState() {
        detailViewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            updateFavoriteButtonUI(isFavorite)
        }
    }

    private fun observeSelectedTab() {
        detailViewModel.selectedTab.observe(viewLifecycleOwner) { tabIndex ->
            val fragment = when (tabIndex) {
                TAB_LAYOUT_LEFT -> IngredientsFragment
                    .newInstance(
                        detailViewModel.resultCocktails.value?.drinks?.firstOrNull()?.ingredients?.toParcelable()
                            ?: emptyList()
                    )

                TAB_LAYOUT_RIGHT -> InstructionsFragment
                    .newInstance(
                        detailViewModel.resultCocktails.value?.drinks?.firstOrNull()?.instructions ?: ""
                    )

                else -> throw IllegalArgumentException(INVALID_TAB_INDEX)
            }

            childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit()
        }

    }

    private fun observeUiMessageChannel() {
        lifecycleScope.launch {
            detailViewModel.uiMessageChannel.collect {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCocktailDetails() {
        drinkId?.let {
            detailViewModel.getCocktailDetailsById(it)
            detailViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoadingState()
                } else {
                    hideLoadingState()
                }
            }
        }
    }

    private fun showLoadingState() {
        with(binding) {
            detailProgressBar.visibility = View.VISIBLE
            contentLayout.detailScrollView.visibility = View.GONE
        }
    }

    private fun hideLoadingState() {
        with(binding) {
            detailProgressBar.visibility = View.GONE
            contentLayout.detailScrollView.visibility = View.VISIBLE
        }
    }

    private fun updateUI(cocktails: Cocktails) {
        val drink = cocktails.drinks.first()
        with(binding.contentLayout) {
            cocktailTitleDetails.text = drink.name
            Glide.with(this@DetailFragment).load(drink.thumb).into(cocktailImageDetails)
            category.text = getString(R.string.category, drink.category, drink.alcoholic)
        }
    }

    private fun updateFavoriteButtonUI(isFavorite: Boolean) {
        binding.contentLayout.btnFavorite.isSelected = isFavorite
    }

    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        private const val INVALID_TAB_INDEX = "Invalid tab index"
        private const val TAB_LAYOUT_LEFT = 0
        private const val TAB_LAYOUT_RIGHT = 1

        @JvmStatic
        fun newInstance(id: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DRINK_ID, id)
            }
        }
    }

}
