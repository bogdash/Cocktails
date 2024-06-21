package com.bogdash.cocktails.presentation.saved.adapter.child

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bogdash.domain.models.Drink

class DrinkAdapter (
    private val action: (String) -> Unit
) : ListAdapter<Drink, DrinkViewHolder>(DrinkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DrinkViewHolder.create(parent, action)

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<Drink>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}