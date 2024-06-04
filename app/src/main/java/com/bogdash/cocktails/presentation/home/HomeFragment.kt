package com.bogdash.cocktails.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentHomeScreenBinding
import com.bogdash.cocktails.presentation.detail.adapter.IngredientAdapter
import com.bogdash.cocktails.presentation.home.adapter.HomeItemsAdapter
import com.bogdash.cocktails.presentation.main.MainViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeScreenBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //val coctails = homeViewModel.GetCocktailsByPage()
        //binding.recyclerView.adapter = HomeItemsAdapter(coctails)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
