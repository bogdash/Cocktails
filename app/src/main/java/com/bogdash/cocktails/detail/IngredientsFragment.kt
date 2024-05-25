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
import com.bogdash.cocktails.model.Ingredient

class IngredientsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientAdapter
    private val ingredientsList = arrayListOf(
        Ingredient(1, "Tequila", "4.5 cl"),
        Ingredient(2, "Lime Juice", "1.5 cl"),
        Ingredient(3, "Agave syrup", "2 spoons")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ingredients_fragment, container, false)

        recyclerView = view.findViewById(R.id.rcIngredients)
        adapter = IngredientAdapter(ingredientsList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = IngredientsFragment()
    }
}