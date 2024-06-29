package com.bogdash.cocktails.presentation.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bogdash.domain.models.Drink

class SearchDiffCallback : DiffUtil.ItemCallback<Drink>() {

    override fun areItemsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem == newItem

}
