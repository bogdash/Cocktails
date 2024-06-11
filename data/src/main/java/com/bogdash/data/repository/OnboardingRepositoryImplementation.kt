package com.bogdash.data.repository

import android.content.SharedPreferences
import com.bogdash.data.storage.preferences.ConstantsForSharedPreferences
import com.bogdash.domain.repository.OnboardingRepository

class OnboardingRepositoryImplementation(private val sharedPreferences: SharedPreferences) :
    OnboardingRepository {

    override var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(ConstantsForSharedPreferences.KEY_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit().putBoolean(ConstantsForSharedPreferences.KEY_FIRST_LAUNCH, value).apply()

}
