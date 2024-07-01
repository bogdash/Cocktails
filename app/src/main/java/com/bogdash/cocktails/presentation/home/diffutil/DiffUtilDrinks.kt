package com.bogdash.cocktails.presentation.home.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.bogdash.domain.models.Drink

class DiffUtilDrinks(
    private val oldLIst: List<Drink>,
    private val newList: List<Drink>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLIst.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldLIst[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldLIst[oldItemPosition].id == newList[newItemPosition].id &&
                oldLIst[oldItemPosition].name == newList[newItemPosition].name &&
                oldLIst[oldItemPosition].thumb == newList[newItemPosition].thumb
    }

}