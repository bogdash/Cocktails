package com.bogdash.cocktails.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.databinding.ItemSearchRvBinding
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide

class SearchViewHolder(
    private val binding: ItemSearchRvBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(drink: Drink) {
        val context = this.itemView.context
        with(binding) {
            titleSearchRv.text = drink.name
            Glide.with(context).load(drink.thumb).into(imgSearchRv)
        }
        itemView.setOnClickListener {
            action(drink.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = SearchViewHolder(
            ItemSearchRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }

}
