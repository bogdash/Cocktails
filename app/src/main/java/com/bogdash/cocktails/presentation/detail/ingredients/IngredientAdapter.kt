package com.bogdash.cocktails.presentation.detail.ingredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.IngredientItemBinding
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient
import com.bogdash.domain.models.Ingredient

class IngredientAdapter(private var ingredientsList: List<ParcelableIngredient>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(private val binding: IngredientItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: ParcelableIngredient) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientMeasure.text = ingredient.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun updateList(newList: List<ParcelableIngredient>) {
        ingredientsList = newList
        notifyDataSetChanged()
    }
}
