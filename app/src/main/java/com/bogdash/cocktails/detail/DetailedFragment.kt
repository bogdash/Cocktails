package com.bogdash.cocktails.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailedBinding
import com.google.android.material.tabs.TabLayout

class DetailedFragment : Fragment() {

    private lateinit var binding: FragmentDetailedBinding
    private val fragmentList = listOf(
        IngredientsFragment.newInstance(),
        DirectionsFragment.newInstance()
    )
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("DetailedFragment", "onCreateView called")
        binding = FragmentDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailedFragment", "onViewCreated called")

        // Установка начального фрагмента
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragmentList[0]).commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = fragmentList[tab.position]
                childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()

                if (tab.position == 1) {
                    view.post {
                        if (fragment.isAdded) {
                            (fragment as DirectionsFragment).updateInstruction(
                                "Rub the rim of the glass with the lime slice to make the salt stick to it. " +
                                        "Take care to moisten only the outer rim and sprinkle the salt on it. " +
                                        "The salt should present to the lips of the imbiber and never mix into the cocktail. " +
                                        "Shake the other ingredients with ice, then carefully pour into the glass."
                            )
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            binding.btnFavorite.isSelected = isFavorite
            // TODO: логика сохранения состояния избранного
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailedFragment()
    }
}