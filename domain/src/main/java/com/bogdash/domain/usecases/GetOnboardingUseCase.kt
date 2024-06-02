package com.bogdash.domain.usecases

import com.bogdash.domain.repository.OnboardingRepository

class GetOnboardingUseCase(private val onboardingRepository: OnboardingRepository) {

    fun execute(): Boolean {
        val isFirstLaunch = onboardingRepository.isFirstLaunch

        if (isFirstLaunch) onboardingRepository.isFirstLaunch = false

        return isFirstLaunch
    }

}
