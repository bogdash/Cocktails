package com.bogdash.cocktails.presentation.saved.adapter.parent

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView


class ListDividerItemDecoration(
    @ColorInt private val color: Int,
    @Px private val heightPx: Int,
    private val isLast: (RecyclerView.ViewHolder, Int) -> Boolean = { viewHolder: RecyclerView.ViewHolder,
        size: Int ->
        viewHolder.adapterPosition == size - 1
    }
) : RecyclerView.ItemDecoration(){

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            this.color = this@ListDividerItemDecoration.color
            this.strokeWidth = this@ListDividerItemDecoration.heightPx.toFloat()
        }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.let { adapter ->
            parent.children.forEach { view ->
                val viewHolder = parent.getChildViewHolder(view)
                if (!isLast(viewHolder,adapter.itemCount)) {
                    val y = (view.top + view.height).toFloat()
                    val (startX, startY) = 80f to y
                    val (stopX, stopY) = (view.width.toFloat() - 80f) to y
                    c.drawLine(startX, startY, stopX, stopY, paint)
                }
            }
        }
    }
}