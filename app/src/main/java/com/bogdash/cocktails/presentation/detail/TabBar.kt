package com.bogdash.cocktails.presentation.detail

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.LayoutTabBarBinding

/**
 * Custom tab bar view for displaying tabs with an indicator.
 *
 * @param context the context in which the view is running
 * @param attrs the attributes of the XML tag that is inflating the view
 */
typealias OnTabSelectedListener = ((Int) -> Unit)?

class TabBar(context: Context?, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private lateinit var listTabName: List<String>
    private var binding: LayoutTabBarBinding
    private var listTabTv: List<TextView> = emptyList()
    private var onTabSelectedListener: OnTabSelectedListener = null

    init {
        binding = LayoutTabBarBinding.inflate(LayoutInflater.from(context), this, true)
        setupAttrs(attrs)
        setupUI()
    }

    /**
     * Sets a listener that will be notified when a tab is selected.
     *
     * @param listener the listener to notify
     */
    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
        onTabSelectedListener = listener
    }

    /**
     * Initializes the attributes from the XML layout.
     *
     * @param attrs the attributes of the XML tag that is inflating the view
     */
    private fun setupAttrs(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.TabBar,
            0, 0
        )

        listTabName = typedArray
            .getTextArray(R.styleable.TabBar_android_entries)
            .toList().map {
                it.toString()
            }

        typedArray.recycle()
    }

    /**
     * Sets up the user interface og the tab bar.
     */
    private fun setupUI() {
        listTabTv = listTabName.mapIndexed { index, tabName ->
            initTabTv(tabName, index)
        }

        binding.viewTabsWrapper.apply {
            weightSum = listTabTv.size.toFloat()
            listTabTv.forEach {
                this.addView(it)
            }
        }

        binding.viewIndicatorWrapper.apply {
            weightSum = listTabTv.size.toFloat()
        }

        onTabSelected(0)
    }

    /**
     * Initializes a TextView for a tab.
     *
     * @param tabName the name of the tab
     * @param index the index of the tab
     * @return the initialized TextView
     */
    private fun initTabTv(tabName: String, index: Int) = TextView(context).apply {
        text = tabName
        layoutParams = LinearLayout.LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            1f
        )
        gravity = Gravity.CENTER
        setTextColor(ContextCompat.getColor(this.context, R.color.dark_gray))
        textSize = 14f

        setOnClickListener {
            onTabSelected(index)
        }

        listTabTv.forEach {
            if (it == listTabTv[index]) {
                it.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                it.setTextColor(ContextCompat.getColor(this.context, R.color.dark_gray))
            }
        }
    }

    /**
     * Handles the event of selecting a tab.
     *
     * @param index the index of the selected tab
     */
    private fun onTabSelected(index: Int) {
        onTabSelectedListener?.let { it(index) }
        ObjectAnimator.ofFloat(
            binding.viewIndicator,
            View.TRANSLATION_X,
            binding.viewIndicator.x,
            listTabTv[index].x
        ).apply {
            duration = 300
            start()
        }

        listTabTv.forEach {
            if (it === listTabTv[index]) {
                it.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                it.setTextColor(ContextCompat.getColor(this.context, R.color.dark_gray))
            }
        }
    }

}
