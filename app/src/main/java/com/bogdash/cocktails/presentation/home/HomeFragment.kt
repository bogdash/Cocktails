package com.bogdash.cocktails.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentHomeScreenBinding
import com.bogdash.cocktails.presentation.detail.adapter.IngredientAdapter
import com.bogdash.cocktails.presentation.home.adapter.HomeItemsAdapter
import com.bogdash.cocktails.presentation.main.MainViewModel
import com.bogdash.domain.models.Cocktails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeItemsAdapter: HomeItemsAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setupRecyclerView()
        homeViewModel.GetCocktailsByPage()
    }

    private fun setupRecyclerView() {
        homeItemsAdapter = HomeItemsAdapter(Cocktails(emptyList()))
        binding.recyclerView.apply {
            adapter = homeItemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.resultCocktailsByPage.observe(viewLifecycleOwner) { cocktails ->
                homeItemsAdapter.updateCocktails(cocktails)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
