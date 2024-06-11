package com.bogdash.cocktails.presentation.detail.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogdash.cocktails.databinding.FragmentIngredientsBinding
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient

class IngredientsFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { it ->
            val ingredients = it.getParcelableArrayList<ParcelableIngredient>(ARG_INGREDIENTS)
            ingredients?.let {
                updateIngredientsList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rcIngredients.layoutManager = LinearLayoutManager(context)
        ingredientAdapter = IngredientAdapter(emptyList())
        binding.rcIngredients.adapter = ingredientAdapter
    }

    private fun updateIngredientsList(ingredients: List<ParcelableIngredient>) {
        ingredientAdapter.updateList(ingredients)
    }

    companion object {
        private const val ARG_INGREDIENTS = "ingredients"

        @JvmStatic
        fun newInstance(ingredients: List<ParcelableIngredient>): IngredientsFragment {
            val fragment = IngredientsFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_INGREDIENTS, ArrayList(ingredients))
            }
            return fragment
        }
    }
}
