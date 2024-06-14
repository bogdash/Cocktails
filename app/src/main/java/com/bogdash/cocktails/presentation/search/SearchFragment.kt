package com.bogdash.cocktails.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
            openDetailedFragment(it)
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
            searchViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoadingState()
                } else {
                    hideLoadingState()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.resultCocktails.observe(viewLifecycleOwner) {
                with(binding){
                    searchRv.visibility = View.VISIBLE
                    tvNoQueries.visibility = View.GONE
                    ivErrorPic.visibility = View.GONE
                }
                setAdapter(it.drinks)
            }
        }
        lifecycleScope.launch{
            searchViewModel.uiMessageChannel.collect {
                with(binding){
                    searchRv.visibility = View.GONE
                    tvNoQueries.visibility = View.VISIBLE
                    tvNoQueries.text = getString(it)
                    ivErrorPic.visibility = View.VISIBLE
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
                if(newText.isEmpty()){
                    with(binding){
                        searchRv.visibility = View.GONE
                        ivErrorPic.visibility = View.VISIBLE
                        tvNoQueries.visibility = View.GONE
                    }
                }
                else{
                    searchViewModel.searchCocktailsByName(newText)
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
        searchAdapter.submitList(list.toMutableList())
    }
    private fun showLoadingState() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            searchRv.visibility = View.GONE
        }
    }
    private fun hideLoadingState() {
        with(binding) {
            progressBar.visibility = View.GONE
        }
    }
    private fun openDetailedFragment(id: String){
        val arguments = bundleOf(ARG_DRINK_ID to id)
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(R.id.fragment_container,DetailFragment::class.java,arguments)
            .addToBackStack(null)
            .commit()
    }
    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        private const val SPAN_COUNT = 2
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}