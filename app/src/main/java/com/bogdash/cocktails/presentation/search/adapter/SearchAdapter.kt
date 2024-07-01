package com.bogdash.cocktails.presentation.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bogdash.domain.models.Drink

class SearchAdapter(
    private val action: (String) -> Unit
) : ListAdapter<Drink, SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder.create(parent, action)

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animate()
    }

    override fun submitList(list: MutableList<Drink>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }

}
