package com.bogdash.cocktails.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.CocktailCardHomescreenItemBinding
import com.bogdash.cocktails.presentation.home.diffutil.DiffUtilDrinks
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide

class HomeItemsAdapter(private var drinks: List<Drink>, private val listener: Listener) :
    RecyclerView.Adapter<HomeItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = CocktailCardHomescreenItemBinding.bind(item)
        fun bind(drink: Drink, listener: Listener) {
            val context = this.itemView.context
            with(binding) {
                tvTitle.text = drink.name
                Glide.with(context).load(drink.thumb).into(imageView)
                itemView.setOnClickListener {
                    listener.onClick(drink)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cocktail_card_homescreen_item, parent, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(drinks[position], listener)
    }

    fun updateCocktails(newDrinks: List<Drink>) {
        val diffUtilDrinks = DiffUtilDrinks(drinks, newDrinks)
        val diffResults = DiffUtil.calculateDiff(diffUtilDrinks)
        this.drinks = newDrinks
        diffResults.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun onClick(drink: Drink)
    }
}