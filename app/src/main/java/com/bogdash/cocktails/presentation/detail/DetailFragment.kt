package com.bogdash.cocktails.presentation.detail

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.Constants.DetailFragment.FROM_SCANNER
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentDetailBinding
import com.bogdash.cocktails.presentation.detail.ingredients.IngredientsFragment
import com.bogdash.cocktails.presentation.detail.instructions.InstructionsFragment
import com.bogdash.cocktails.presentation.exceptions.ExceptionFragment
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment(input: Input) : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var qrDialog: AlertDialog? = null
    private var initCocktailInViewModel: (() -> Unit)? = null

    init {
        initCocktailInViewModel = {
            when (input) {
                is Input.Id -> {
                    viewModel.getCocktailDetailsById(input.id)
                }

                is Input.Json -> {
                    viewModel.getCocktailDetailsByJson(input.json)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCocktailInViewModel?.let { it() }
    }

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

        val fromScanner = arguments?.getBoolean(FROM_SCANNER, false) ?: false
        if (fromScanner) {
            binding.contentLayout.btnBack.isVisible = false
            binding.contentLayout.cocktailTitleDetails.gravity = Gravity.START
        }
    }

    override fun onStop() {
        super.onStop()
        qrDialog?.dismiss()
        qrDialog = null
    }

    private fun initListeners() {
        initTabLayout()

        val animAlpha = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)

        with(binding.contentLayout) {
            btnBack.setOnClickListener {
                it.startAnimation(animAlpha)
                parentFragmentManager.popBackStack()
            }

            btnFavorite.setOnClickListener {
                it.startAnimation(animAlpha)
                viewModel.toggleFavorite()
            }

            btnShare.setOnClickListener {
                it.startAnimation(animAlpha)
                initQR()
            }
        }
    }

    private fun initQR() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.qr_dialog, null)
        val imageView = view.findViewById<ImageView>(R.id.iv_qr)
        imageView.setImageBitmap(viewModel.qr)
        builder.setView(view)

        qrDialog = builder.create()
        qrDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        qrDialog?.show()
    }

    private fun initTabLayout() {
        binding.contentLayout.tabLayout.setOnTabSelectedListener { index ->
            viewModel.setSelectedTab(index)
        }
    }

    private fun observeViewModel() {
        observeResultCocktails()
        observeFavoriteState()
        observeSelectedTab()
        observeUiMessageChannel()
        observeLoadingState()
    }

    private fun observeResultCocktails() {
        lifecycleScope.launch {
            viewModel.cocktail.collectLatest { cocktail ->
                updateUI(cocktail)
                viewModel.setSelectedTab(DetailViewModel.TAB_LEFT)
            }
        }
    }

    private fun observeFavoriteState() {
        lifecycleScope.launch {
            viewModel.isFavorite.collectLatest { isFavorite ->
                updateFavoriteButtonUI(isFavorite)
            }
        }
    }

    private fun observeSelectedTab() {
        lifecycleScope.launch {
            viewModel.selectedTab.collectLatest { tab ->
                viewModel.cocktail.collectLatest { cocktail ->
                    val fragment = when (tab) {
                        DetailViewModel.Tab.INGREDIENTS -> IngredientsFragment.newInstance(cocktail)
                        DetailViewModel.Tab.INSTRUCTIONS -> InstructionsFragment.newInstance(cocktail)
                    }

                    replaceFragment(fragment)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun observeUiMessageChannel() {
        lifecycleScope.launch {
            viewModel.uiMessageChannel.collectLatest {
                openExceptionFragment(getString(it))
            }
        }
    }

    private fun openExceptionFragment(exText: String) {
        val fragment = ExceptionFragment.newInstance(exText)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
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
            detailProgressBar.isVisible = true
            contentLayout.detailScrollView.isVisible = false
        }
    }

    private fun hideLoadingState() {
        with(binding) {
            detailProgressBar.isVisible = false
            contentLayout.detailScrollView.isVisible = true
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

    sealed class Input {
        class Id(val id: String) : Input()
        class Json(val json: String) : Input()
    }

}
