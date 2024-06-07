package com.bogdash.cocktails.presentation.detail.ingredients

import androidx.recyclerview.widget.DiffUtil
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient

class IngredientDiffCallback(
    private val oldList: List<ParcelableIngredient>,
    private val newList: List<ParcelableIngredient>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}
