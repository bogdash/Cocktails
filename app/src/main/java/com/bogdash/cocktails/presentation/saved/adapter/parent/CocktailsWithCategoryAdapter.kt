package com.bogdash.cocktails.presentation.saved.adapter.parent

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bogdash.domain.models.CocktailsWithCategory

class CocktailsWithCategoryAdapter (
    private val onDrinkClicked: (String) -> Unit
) : ListAdapter<CocktailsWithCategory, CocktailsWithCategoryViewHolder>(CocktailsWithCategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CocktailsWithCategoryViewHolder.create(parent, onDrinkClicked)

    override fun onBindViewHolder(holder: CocktailsWithCategoryViewHolder, position: Int) =
       holder.bind(getItem(position))

    override fun submitList(list: MutableList<CocktailsWithCategory>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}