package com.bogdash.cocktails.presentation.splash

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.bogdash.cocktails.Constants
import com.bogdash.cocktails.presentation.main.MainActivity
import com.bogdash.cocktails.presentation.onboarding.OnboardingActivity
import com.bogdash.domain.usecases.GetOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getOnboardingUseCase: GetOnboardingUseCase) :
    ViewModel() {

    fun showNextScreen(activity: Activity) {
        val isFirstLaunch = getOnboardingUseCase.execute()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (isFirstLaunch) firstLaunch(activity) else notFirstLaunch(activity)
        }, Constants.Splash.DELAY_TIME)

    }

    private fun firstLaunch(activity: Activity) {
        val i = Intent(activity, OnboardingActivity::class.java)
        startActivity(activity, i, null)
        activity.finish()
    }

    private fun notFirstLaunch(activity: Activity) {
        val i = Intent(activity, MainActivity::class.java)
        startActivity(activity, i, null)
        activity.finish()
    }

}
