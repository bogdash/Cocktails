package com.bogdash.cocktails.presentation.detail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailBinding
import com.bogdash.cocktails.presentation.detail.instructions.InstructionsFragment
import com.bogdash.cocktails.presentation.detail.ingredients.IngredientsFragment
import com.bogdash.cocktails.presentation.detail.models.mappers.toParcelable
import com.bogdash.cocktails.presentation.detail.qrCodeDecoder.QRCodeEncoder
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.zip.Inflater

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var drinkString: String? = null
    private lateinit var inputType: Input.Type
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        observeViewModel()
        loadCocktailDetails()
    }

    private fun initListeners() {
        initTabLayout()
        val animAlpha = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
        with(binding.contentLayout) {
            btnBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            btnFavorite.setOnClickListener {
                it.startAnimation(animAlpha)
                detailViewModel.toggleFavorite()
            }
        }
        initFab()
    }

    private fun getBitmapFromString(s: String): Bitmap {
        return QRCodeEncoder(requireContext()).encodeAsBitmap(s, 700)!!
    }

    private fun initFab() {
        binding.contentLayout.floatActionButton.setOnClickListener {
            val serializedDrink = Json.encodeToString(detailViewModel.getCurrentDrink())
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val cv = requireActivity().layoutInflater.inflate(R.layout.qr_dialog, null)
            val iv = cv.findViewById<ImageView>(R.id.iv_qr)
            iv.setImageBitmap(getBitmapFromString(serializedDrink))
            builder.setView(cv)

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun initTabLayout() {
        binding.contentLayout.tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(index: Int) {
                detailViewModel.setSelectedTab(index)
            }
        })
    }

    private fun observeViewModel() {
            observeResultCocktails()
            observeFavoriteState()
            observeSelectedTab()
            observeUiMessageChannel()
    }

    private fun observeResultCocktails() {
        detailViewModel.resultCocktails.observe(viewLifecycleOwner) {
            it?.let {
                updateUI(it)
                detailViewModel.setSelectedTab(TAB_LAYOUT_LEFT)
            }
        }
    }

    private fun observeFavoriteState() {
        detailViewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            updateFavoriteButtonUI(isFavorite)
        }
    }

    private fun observeSelectedTab() {
        detailViewModel.selectedTab.observe(viewLifecycleOwner) { tabIndex ->
            val fragment = when (tabIndex) {
                TAB_LAYOUT_LEFT -> IngredientsFragment
                    .newInstance(
                        detailViewModel.resultCocktails.value?.ingredients?.toParcelable()
                            ?: emptyList()
                    )

                TAB_LAYOUT_RIGHT -> InstructionsFragment
                    .newInstance(
                        detailViewModel.resultCocktails.value?.instructions ?: ""
                    )

                else -> throw IllegalArgumentException(INVALID_TAB_INDEX)
            }

            childFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }

    }

    private fun observeUiMessageChannel() {
        lifecycleScope.launch {
            detailViewModel.uiMessageChannel.collect {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCocktailDetails() {
        drinkString?.let {

            when(inputType) {
                Input.Type.ID -> {
                    detailViewModel.getCocktailDetailsById(it)
                }
                Input.Type.JSON -> {
                    detailViewModel.getCocktailDetailsByJson(it)
                }
            }

            detailViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoadingState()
                } else {
                    hideLoadingState()
                }
            }
        }
    }

    private fun showLoadingState() {
        with(binding) {
            detailProgressBar.visibility = View.VISIBLE
            contentLayout.detailScrollView.visibility = View.GONE
        }
    }

    private fun hideLoadingState() {
        with(binding) {
            detailProgressBar.visibility = View.GONE
            contentLayout.detailScrollView.visibility = View.VISIBLE
        }
    }

    private fun updateUI(drink: Drink) {
        with(binding.contentLayout) {
            cocktailTitleDetails.text = drink.name
            Glide.with(this@DetailFragment).load(drink.thumb).into(cocktailImageDetails)
            category.text = getString(R.string.category, drink.category, drink.alcoholic)
        }
    }

    private fun updateFavoriteButtonUI(isFavorite: Boolean) {
        binding.contentLayout.btnFavorite.isSelected = isFavorite
    }

    companion object {
        private const val ARG_DRINK_ID = "drink_id"
        private const val INVALID_TAB_INDEX = "Invalid tab index"
        private const val TAB_LAYOUT_LEFT = 0
        private const val TAB_LAYOUT_RIGHT = 1

        @JvmStatic
        fun newInstance(input: Input) = DetailFragment().apply {
            when(input) {
                is Input.Id -> {
                    drinkString = input.id
                    inputType = Input.Type.ID
                }
                is Input.Json -> {
                    drinkString = input.json
                    inputType = Input.Type.JSON
                }
            }
        }
    }

    sealed class Input {
        class Id(val id: String): Input()
        class Json(val json: String): Input()

        enum class Type {
            ID, JSON
        }
    }

}
