package com.bogdash.cocktails.presentation.saved.adapter.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.databinding.ItemParentSavedRvBinding
import com.bogdash.cocktails.presentation.saved.adapter.child.DrinkAdapter
import com.bogdash.domain.models.CocktailsWithCategory

class CocktailsWithCategoryViewHolder (
    private val binding: ItemParentSavedRvBinding,
    onDrinkClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val drinkAdapter = DrinkAdapter(
        action = onDrinkClicked
    )

    fun bind(cocktailsWithCategory: CocktailsWithCategory) {
        val context = this.itemView.context
        with(binding) {
            titleSavedCategory.text = cocktailsWithCategory.category
            childSavedRv.apply{
                adapter = drinkAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            }
            drinkAdapter.submitList(cocktailsWithCategory.cocktails.drinks.toMutableList())
        }
    }
    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = CocktailsWithCategoryViewHolder(
            ItemParentSavedRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }
}
