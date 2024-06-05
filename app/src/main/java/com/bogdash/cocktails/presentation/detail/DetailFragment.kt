package com.bogdash.cocktails.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailBinding
import com.bogdash.cocktails.presentation.detail.directions.DirectionsFragment
import com.bogdash.cocktails.presentation.detail.ingredients.IngredientsFragment
import com.google.android.material.tabs.TabLayout

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val fragmentList = listOf(
        IngredientsFragment.newInstance(),
        DirectionsFragment.newInstance()
    )
    private var isFavorite = false
    private var drinkId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            drinkId = it.getString(ARG_DRINK_ID)
        }

        // Start fragment
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
            parentFragmentManager.popBackStack()
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            binding.btnFavorite.isSelected = isFavorite
            // TODO: Add logic saved actions
        }
    }

    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        @JvmStatic
        fun newInstance(id: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DRINK_ID, id)
            }
        }
    }
}
