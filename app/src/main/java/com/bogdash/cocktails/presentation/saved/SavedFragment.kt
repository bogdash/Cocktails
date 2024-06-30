package com.bogdash.cocktails.presentation.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.Constants.Saved.FROM_SAVED
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSavedBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.exceptions.ExceptionFragment
import com.bogdash.cocktails.presentation.saved.adapter.parent.CocktailsWithCategoryAdapter
import com.bogdash.cocktails.presentation.saved.adapter.parent.ListDividerItemDecoration
import com.bogdash.domain.models.CocktailsWithCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.bogdash.cocktails.presentation.detail.DetailFragment.Input.Id

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var cocktailsWithCategoryAdapter: CocktailsWithCategoryAdapter
    private val savedViewModel: SavedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cocktailsWithCategoryAdapter = CocktailsWithCategoryAdapter {
            openDetailedFragment(it)
        }
        savedViewModel.getCocktailsWithCategories()
        initObservers()
        initRecycler()
    }

    private fun initObservers() {
        savedViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingState()
            } else {
                hideLoadingState()
            }
        }
        savedViewModel.resultCocktailsWithCategories.observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                openExceptionFragment(getString(R.string.no_saved_cocktails))
            } else {
                setAdapter(it)
            }

        }
        lifecycleScope.launch{
            savedViewModel.uiMessageChannel.collect {
                openExceptionFragment(getString(it))
            }
        }
    }
    private fun initRecycler() {
        binding.parentSavedRv.apply {
            adapter = cocktailsWithCategoryAdapter
            addItemDecoration(
                ListDividerItemDecoration(
                    color = requireContext().getColor(R.color.ghost),
                    heightPx = 2
                )
            )
        }
    }
    private fun setAdapter(list: List<CocktailsWithCategory>) {
        cocktailsWithCategoryAdapter.submitList(list.toMutableList())
    }
    private fun showLoadingState() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            parentSavedRv.visibility = View.GONE
        }
    }
    private fun hideLoadingState() {
        with(binding) {
            progressBar.visibility = View.GONE
            parentSavedRv.visibility = View.VISIBLE
        }
    }
    private fun openDetailedFragment(id: String){
        val fragment = DetailFragment(Id(id))
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(FROM_SAVED)
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
        fun newInstance() = SavedFragment()
    }
}
