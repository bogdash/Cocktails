package com.bogdash.cocktails.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.adapter.IngredientAdapter
import com.bogdash.cocktails.databinding.FragmentIngredientsBinding
import com.bogdash.cocktails.model.Ingredient

class IngredientsFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsBinding

    private val ingredientsList = arrayListOf(
        Ingredient("Tequila", "4.5 cl"),
        Ingredient("Lime Juice", "1.5 cl"),
        Ingredient( "Agave syrup", "2 spoons")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        binding.rcIngredients.layoutManager = LinearLayoutManager(context)
        binding.rcIngredients.adapter = IngredientAdapter(ingredientsList)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = IngredientsFragment()
    }
}