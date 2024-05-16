package com.bogdash.cocktails.filters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.R

<<<<<<<< HEAD:app/src/main/java/com/bogdash/cocktails/filters/FiltersFragment.kt
class FiltersFragment : Fragment() {
========
class HomeScreenFragment : Fragment() {
>>>>>>>> master:app/src/main/java/com/bogdash/cocktails/filters/HomeScreenFragment.kt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    companion object {
        @JvmStatic
<<<<<<<< HEAD:app/src/main/java/com/bogdash/cocktails/filters/FiltersFragment.kt
        fun newInstance() = FiltersFragment()
========
        fun newInstance() = HomeScreenFragment()
>>>>>>>> master:app/src/main/java/com/bogdash/cocktails/filters/HomeScreenFragment.kt
    }
}