package com.bogdash.cocktails.presentation.detail.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.databinding.IngredientItemBinding
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient

class IngredientAdapter(private var ingredientsList: List<ParcelableIngredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(private val binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: ParcelableIngredient) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientMeasure.text = ingredient.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding =
            IngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun updateList(newList: List<ParcelableIngredient>) {
        val diffCallback = IngredientDiffCallback(ingredientsList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        ingredientsList = newList
        diffResult.dispatchUpdatesTo(this)
    }

}
