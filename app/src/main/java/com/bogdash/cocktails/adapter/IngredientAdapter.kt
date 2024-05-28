package com.bogdash.cocktails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.IngredientItemBinding
import com.bogdash.cocktails.model.Ingredient

class IngredientAdapter(private val ingredientsList: ArrayList<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.IngredientHolder>() {

    class IngredientHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = IngredientItemBinding.bind(item)
        fun bind(ingredient: Ingredient) = with(binding) {
            ingredientName.text = ingredient.ingredientName
            ingredientMeasure.text = ingredient.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }
}