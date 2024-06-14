package com.bogdash.cocktails.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentHomeScreenBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.home.filters.FilterHandler
import com.bogdash.cocktails.presentation.home.adapter.HomeItemsAdapter
import com.bogdash.domain.models.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_screen), HomeItemsAdapter.Listener {

    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeItemsAdapter: HomeItemsAdapter
    private lateinit var filterHandler: FilterHandler
    private val homeViewModel: HomeViewModel by viewModels()

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
        initObservers()
        initListeners()
        setupRecyclerView()
        homeViewModel.loadInitialCocktails()
    }

    private fun setupRecyclerView() {
        homeItemsAdapter = HomeItemsAdapter(emptyList(), this)
        binding.recyclerView.apply {
            adapter = homeItemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.resultCocktails.observe(viewLifecycleOwner) { cocktails ->
                homeItemsAdapter.updateCocktails(cocktails.drinks)
                binding.progressBar.visibility =
                    if (cocktails.drinks.isEmpty()) View.VISIBLE else View.GONE
            }

            homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading && homeItemsAdapter.itemCount == 0) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

            homeViewModel.alcoholicFilterType.observe(viewLifecycleOwner) { filterType ->
                homeViewModel.getFilteredCocktailsByAlcoholType(filterType)
            }

            homeViewModel.ingredientsFilterType.observe(viewLifecycleOwner) { ingredients ->
                homeViewModel.getFilteredCocktailsByIngredients(ingredients)
            }

            lifecycleScope.launch {
                homeViewModel.uiMessageChannel.collect {
                    Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initListeners() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        val fragment = DetailFragment.newInstance(drinkId)
        parentFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
