package com.bogdash.cocktails.presentation.search

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent.ACTION_DOWN
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSearchBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.detail.DetailFragment.Input.Id
import com.bogdash.cocktails.presentation.exceptions.ExceptionFragment
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
    ): View {
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
        openExceptionFragment(getString(R.string.error_search))
    }

    private fun setupSearchView(){
        val searchView = binding.searchSv
        val txtSearch = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        txtSearch.setHintTextColor(resources.getColor(R.color.submarine,null))
        txtSearch.setTextColor(resources.getColor(R.color.black,null))
    }
    private fun initObservers() {
        searchViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingState()
            } else {
                hideLoadingState()
            }
        }
        searchViewModel.resultCocktails.observe(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
            setAdapter(it.drinks)
        }
        lifecycleScope.launch{
            searchViewModel.uiMessageChannel.collect {
                binding.searchRv.isVisible = false
                parentFragmentManager.popBackStack()
                openExceptionFragment(getString(it))
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
                    binding.searchRv.isVisible = false
                    parentFragmentManager.popBackStack()
                    openExceptionFragment(getString(R.string.error_search))
                }
                else{
                    searchViewModel.searchCocktailsByName(newText)
                }
                return true
            }
        })

        initClickView()
    }
    private fun initRecycler() {
        binding.searchRv.apply{
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }
    private fun setAdapter(list: List<Drink>) {
        searchAdapter.submitList(list.toMutableList())
        binding.searchRv.isVisible = true
    }
    private fun showLoadingState() {
        with(binding) {
            progressBar.isVisible = true
            searchRv.isVisible = false
        }
    }
    private fun hideLoadingState() {
        with(binding) {
            progressBar.isVisible = false
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initClickView(){
        with(binding){
            searchRv.setOnTouchListener { view, event ->
                if (event.action == ACTION_MOVE) {
                    hideKeyboard(view)
                }
                false
            }
            rootContainer.setOnTouchListener{view, event ->
                if (event.action == ACTION_DOWN) {
                    hideKeyboard(view)
                }
                false
            }
        }
    }
    private fun hideKeyboard(view: View){
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun openDetailedFragment(id: String){
        hideKeyboard(requireActivity().window.decorView)
        val fragment = DetailFragment(Id(id))
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun openExceptionFragment(exText: String) {
        val fragment = ExceptionFragment.newInstance(exText)
        parentFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    companion object {
        private const val SPAN_COUNT = 2
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}