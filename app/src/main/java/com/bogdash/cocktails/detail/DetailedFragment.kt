package com.bogdash.cocktails.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.R
import com.google.android.material.tabs.TabLayout

class DetailedFragment : Fragment() {

    private val fragmentList = listOf(
        IngredientsFragment.newInstance(),
        DirectionsFragment.newInstance(),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detailed_fragment, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragmentList[0]).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragmentList[tab?.position!!]).commit()
                if (tab?.position == 1) {
                    (fragmentList[1] as DirectionsFragment).updateInstruction(
                        "Rub the rim of the glass with the lime slice to make the salt stick to it. " +
                                "Take care to moisten only the outer rim and sprinkle the salt on it. " +
                                "The salt should present to the lips of the imbiber and never mix into the cocktail. " +
                                "Shake the other ingredients with ice, then carefully pour into the glass."
                    )
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailedFragment()
    }
}
