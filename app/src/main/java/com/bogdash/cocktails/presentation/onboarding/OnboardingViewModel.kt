package com.bogdash.cocktails.presentation.onboarding

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.bogdash.cocktails.presentation.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    fun showNext(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}
