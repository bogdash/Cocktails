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

class TabBar(context: Context?, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private lateinit var listTabName: List<String>
    private var binding: LayoutTabBarBinding
    private var listTabTv: List<TextView> = emptyList()
    private var tabSelectedListener: OnTabSelectedListener? = null

    init {
        binding = LayoutTabBarBinding.inflate(LayoutInflater.from(context), this, true)
        setupAttrs(attrs)
        setupUI()
    }

    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
        tabSelectedListener = listener
    }

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

    private fun onTabSelected(index: Int) {
        tabSelectedListener?.onTabSelected(index)
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

interface OnTabSelectedListener {
    fun onTabSelected(index: Int)
}