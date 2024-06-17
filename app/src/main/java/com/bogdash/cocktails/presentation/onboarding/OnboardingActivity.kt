package com.bogdash.cocktails.presentation.onboarding

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.onboarding) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initListeners()
        animate()
    }

    private fun initListeners() {
        binding.btnCloseOnboarding.setOnClickListener {
            viewModel.showNext(this)
        }
    }

    private fun animate() {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        binding.ivOnboarding.animation = shake
    }

}
