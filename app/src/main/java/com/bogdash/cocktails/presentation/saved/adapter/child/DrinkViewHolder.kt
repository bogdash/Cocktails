package com.bogdash.cocktails.presentation.saved.adapter.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.databinding.ItemChildSavedRvBinding
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide

class DrinkViewHolder(
    private val binding: ItemChildSavedRvBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(drink: Drink) {
        val context = this.itemView.context
        with(binding) {
            Glide.with(context).load(drink.thumb).into(imgChildSavedRv)
        }
        binding.root.setOnClickListener {
            action(drink.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = DrinkViewHolder(
            ItemChildSavedRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }

}
