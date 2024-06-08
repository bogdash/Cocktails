package com.bogdash.cocktails.presentation.home.diffutil

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.bogdash.domain.models.Drink

class DiffUtilDrinks(
    private val oldLIst: List<Drink>,
    private val newList: List<Drink>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldLIst.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldLIst[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldLIst[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }

            oldLIst[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }

            oldLIst[oldItemPosition].thumb != newList[newItemPosition].thumb -> {
                false
            }

            else -> true
        }
    }
}