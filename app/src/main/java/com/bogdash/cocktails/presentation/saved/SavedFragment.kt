package com.bogdash.cocktails.presentation.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSavedBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.saved.adapter.parent.CocktailsWithCategoryAdapter
import com.bogdash.cocktails.presentation.saved.adapter.parent.ListDividerItemDecoration
import com.bogdash.domain.models.CocktailsWithCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var cocktailsWithCategoryAdapter: CocktailsWithCategoryAdapter
    private val savedViewModel: SavedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        viewLifecycleOwner.lifecycleScope.launch {
            savedViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoadingState()
                } else {
                    hideLoadingState()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            savedViewModel.resultCocktailsWithCategories.observe(viewLifecycleOwner) {
                if (it.isEmpty()){
                    with(binding) {
                        tvNoSaved.visibility = View.VISIBLE
                        tvNoSaved.text = getString(R.string.no_saved_cocktails)
                        ivPicError.visibility = View.VISIBLE
                    }
                } else {
                    with(binding) {
                        tvNoSaved.visibility = View.GONE
                        ivPicError.visibility = View.GONE
                    }
                }
                setAdapter(it)
            }
        }
        lifecycleScope.launch{
            savedViewModel.uiMessageChannel.collect {
                with(binding) {
                    parentSavedRv.visibility = View.GONE
                    tvNoSaved.visibility = View.VISIBLE
                    tvNoSaved.text = getString(it)
                    ivPicError.visibility = View.VISIBLE
                }
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
        val arguments = bundleOf(ARG_DRINK_ID to id)
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container, DetailFragment::class.java,arguments)
            .addToBackStack(FROM_SAVED)
            .commit()
    }
    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        private const val FROM_SAVED = "from_saved"
        @JvmStatic
        fun newInstance() = SavedFragment()
    }
}
