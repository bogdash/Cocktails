package com.bogdash.cocktails.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSearchBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
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
            openDetailedScreen(it)
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
        lifecycleScope.launch{
            searchViewModel.uiMessageChannel.collect {
                with(binding){
                    searchRv.visibility = View.GONE
                    tvNoQueries.visibility = View.VISIBLE
                    tvNoQueries.text = getString(it)
                }
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
                if(newText.isEmpty())
                    binding.searchRv.visibility = View.GONE
                else{
                    searchViewModel.searchCocktailsByName(newText)
                    binding.searchRv.visibility = View.VISIBLE
                }
                return true
            }
        })
    }
    private fun initRecycler() {
        binding.searchRv.apply{
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }
    private fun setAdapter(list: List<Drink>) {
        with(binding){
            searchRv.visibility = View.VISIBLE
            tvNoQueries.visibility = View.GONE
        }
        searchAdapter.submitList(list.toMutableList())
    }
    private fun openDetailedScreen(id: String){
        val arguments = bundleOf(ARG_DRINK_ID to id)
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,DetailFragment::class.java,arguments)
            .addToBackStack(ADD_DETAILED_TO_BS)
            .commit()
    }
    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        private const val ADD_DETAILED_TO_BS = "add_to_back_stack"
        private const val SPAN_COUNT = 2
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}