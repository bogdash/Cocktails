package com.bogdash.cocktails.presentation.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bogdash.domain.models.Drink

class SearchAdapter (
    private val action: (String) -> Unit
) : ListAdapter<Drink, SearchViewHolder>(WordDiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder.create(parent, action)

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<Drink>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}

class WordDiffUtilsCallback : DiffUtil.ItemCallback<Drink>() {

    override fun areItemsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem == newItem
}