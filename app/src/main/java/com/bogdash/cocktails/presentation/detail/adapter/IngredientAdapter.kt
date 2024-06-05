package com.bogdash.cocktails.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.IngredientItemBinding
import com.bogdash.cocktails.presentation.detail.model.Ingredient

class IngredientAdapter(private val ingredientsList: ArrayList<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = IngredientItemBinding.bind(item)
        fun bind(ingredient: Ingredient) = with(binding) {
            ingredientName.text = ingredient.name
            ingredientMeasure.text = ingredient.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }
}
