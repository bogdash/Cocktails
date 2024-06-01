package com.bogdash.cocktails.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSearchBinding
import com.bogdash.cocktails.presentation.search.adapter.SearchAdapter
import com.bogdash.domain.models.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
        searchAdapter = SearchAdapter {
            // TODO open detailed screen
        }
        initObservers()
        initListeners()
        initRecycler()
    }

    private fun setupSearchView(){
        val searchView = binding.searchSv
        val txtSearch = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        txtSearch.setHintTextColor(resources.getColor(R.color.submarine,null))
        txtSearch.setTextColor(resources.getColor(R.color.black,null))
    }
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.resultCocktails.observe(viewLifecycleOwner) {
                setAdapter(it.drinks)
            }
        }
    }
    private fun initListeners() {
        binding.searchSv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchCocktailsByName(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }
    private fun initRecycler() {
        binding.searchRv.apply{
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }
    private fun setAdapter(list: List<Drink>) {
        searchAdapter.submitList(list.toMutableList())
    }
    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}