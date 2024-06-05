package com.bogdash.cocktails.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.CocktailCardHomescreenItemBinding
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide

class HomeItemsAdapter(var cocktails: Cocktails) : RecyclerView.Adapter<HomeItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CocktailCardHomescreenItemBinding.bind(item)
        fun bind(drink: Drink) {
            val context = this.itemView.context
            with(binding) {
                tvTitle.text = drink.name
                tvCountIngredients.text = drink.ingredients.size.toString()
                Glide.with(context).load(drink.thumb).into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cocktail_card_homescreen_item, parent, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cocktails.drinks.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(cocktails.drinks[position])
    }

    fun updateCocktails(newCocktails: Cocktails) {
        this.cocktails = newCocktails
        notifyDataSetChanged()
    }

}