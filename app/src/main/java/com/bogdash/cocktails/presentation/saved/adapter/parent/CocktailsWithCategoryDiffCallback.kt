package com.bogdash.cocktails.presentation.saved.adapter.parent


import androidx.recyclerview.widget.DiffUtil
import com.bogdash.domain.models.CocktailsWithCategory

class CocktailsWithCategoryDiffCallback : DiffUtil.ItemCallback<CocktailsWithCategory>() {
    override fun areItemsTheSame(
        oldItem: CocktailsWithCategory,
        newItem: CocktailsWithCategory
    ): Boolean = oldItem.category == newItem.category && oldItem.cocktails == newItem.cocktails

    override fun areContentsTheSame(
        oldItem: CocktailsWithCategory,
        newItem: CocktailsWithCategory
    ): Boolean = oldItem == newItem
}