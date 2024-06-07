package com.bogdash.cocktails.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailBinding
import com.bogdash.cocktails.presentation.detail.instructions.InstructionsFragment
import com.bogdash.cocktails.presentation.detail.ingredients.IngredientsFragment
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient
import com.bogdash.cocktails.presentation.detail.models.mappers.toParcelable
import com.bogdash.domain.models.Cocktails
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val fragmentList = listOf(
        IngredientsFragment.newInstance(emptyList()),
        InstructionsFragment.newInstance("")
    )
    private var isFavorite = false
    private var drinkId: String? = null
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            drinkId = it.getString(ARG_DRINK_ID)
        }

        // Start fragment
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragmentList[0])
            .commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = fragmentList[tab.position]
                childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                    .commit()

                if (tab.position == 1) {
                    view.post {
                        if (fragment.isAdded) {
                            val drink = detailViewModel.resultCocktails.value?.drinks?.firstOrNull()
                            drink?.instructions?.let { instructions ->
                                val instructionsFragment =
                                    InstructionsFragment.newInstance(instructions)
                                childFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, instructionsFragment)
                                    .commit()
                            }
                        }
                    }
                } else {
                    view.post {
                        if (fragment.isAdded) {
                            val drink = detailViewModel.resultCocktails.value?.drinks?.firstOrNull()
                            drink?.ingredients?.let { ingredients ->
                                val parcelableIngredients = ingredients.toParcelable()
                                val ingredientsFragment = IngredientsFragment.newInstance(parcelableIngredients)
                                childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ingredientsFragment).commit()
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            binding.btnFavorite.isSelected = isFavorite
            // TODO: Add logic saved actions
        }

        observeCocktailDetails()
        loadCocktailDetails()
    }

    private fun observeCocktailDetails() {
        detailViewModel.resultCocktails.observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
                val drink = it.drinks.firstOrNull()
                drink?.ingredients?.let { ingredients ->
                    val parcelableIngredients = ingredients.toParcelable()
                    val ingredientsFragment = IngredientsFragment.newInstance(parcelableIngredients)
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ingredientsFragment)
                        .commit()
                }
            }
        })
    }

    private fun loadCocktailDetails() {
        drinkId?.let {
            detailViewModel.getCocktailDetailsById(it)
        }
    }

    private fun updateUI(cocktails: Cocktails) {
        val drink = cocktails.drinks.first()
        with(binding) {
            cocktailTitleDetails.text = drink.name
            Glide.with(this@DetailFragment).load(drink.thumb).into(cocktailImageDetails)
            category.text = getString(R.string.category, drink.category, drink.alcoholic)
        }

    }

    companion object {
        private const val ARG_DRINK_ID = "drink_id"

        @JvmStatic
        fun newInstance(id: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DRINK_ID, id)
            }
        }
    }
}
