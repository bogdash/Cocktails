package com.bogdash.cocktails.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentHomeScreenBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.exceptions.ExceptionFragment
import com.bogdash.cocktails.presentation.home.filters.FilterHandler
import com.bogdash.cocktails.presentation.detail.DetailFragment.Input.Id
import com.bogdash.cocktails.presentation.home.adapter.HomeItemsAdapter
import com.bogdash.domain.models.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_screen), HomeItemsAdapter.Listener {

    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeItemsAdapter: HomeItemsAdapter
    private lateinit var filterHandler: FilterHandler
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        filterHandler =
            FilterHandler(requireContext(), homeViewModel, layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initListeners()
        setupRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        saveScrollPosition()
    }

    override fun onResume() {
        super.onResume()
        restoreScrollPosition()
    }

    private fun setupRecyclerView() {
        homeItemsAdapter = HomeItemsAdapter(emptyList(), this)
        binding.recyclerViewHomeScreen.apply {
            adapter = homeItemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun saveScrollPosition() {
        val layoutManager = binding.recyclerViewHomeScreen.layoutManager as LinearLayoutManager
        homeViewModel.setScrollPosition(layoutManager.findFirstVisibleItemPosition())
        val view = layoutManager.findViewByPosition(homeViewModel.getScrollPosition())
        homeViewModel.setScrollOffset(view?.top ?: 0)
    }

    private fun restoreScrollPosition() {
        val layoutManager = binding.recyclerViewHomeScreen.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(homeViewModel.getScrollPosition(), homeViewModel.getScrollOffset())
    }

    private fun observeViewModel() {
        observeResultCocktails()
        observeAlcoholicFilter()
        observeIngredientsFilter()
        observeUiMessageChannel()
        observeLoadingCocktails()
    }

    private fun observeResultCocktails() {
        homeViewModel.resultCocktails.observe(viewLifecycleOwner) { cocktails ->
            homeItemsAdapter.updateCocktails(cocktails.drinks)

            if (homeViewModel.getIsFilterChanged()) {
                binding.recyclerViewHomeScreen.scrollToPosition(0)
                homeViewModel.setIsFilterChanged(false)
            }

            binding.progressBar.isVisible = cocktails.drinks.isEmpty()
        }
    }

    private fun observeAlcoholicFilter() {
        homeViewModel.alcoholicFilterType.observe(viewLifecycleOwner) { filterType ->
            homeViewModel.getFilteredCocktailsByAlcoholType(filterType)
        }
    }

    private fun observeIngredientsFilter() {
        homeViewModel.ingredientsFilterType.observe(viewLifecycleOwner) { ingredients ->
            homeViewModel.getFilteredCocktailsByIngredients(ingredients)
        }
    }

    private fun observeUiMessageChannel() {
        lifecycleScope.launch {
            homeViewModel.uiMessageChannel.collect {
                openExceptionFragment(getString(it))
            }
        }
    }

    private fun observeLoadingCocktails() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading && homeItemsAdapter.itemCount == 0) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initListeners() {
        binding.recyclerViewHomeScreen.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    homeViewModel.getNextPageCocktails()
                }
            }
        })

        binding.btnFilter.setOnClickListener {
            filterHandler.showBottomSheetDialog()
        }
    }

    override fun onClick(drink: Drink) {
        openDetailedFragment(drink.id)
    }

    private fun openDetailedFragment(drinkId: String) {
        val fragment = DetailFragment(Id(drinkId))
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openExceptionFragment(exText: String) {
        val fragment = ExceptionFragment.newInstance(exText)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
