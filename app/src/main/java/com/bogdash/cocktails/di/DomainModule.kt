package com.bogdash.cocktails.di

import com.bogdash.domain.repository.OnboardingRepository
import com.bogdash.domain.usecases.GetOnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideGetOnboardingUseCase(repository: OnboardingRepository): GetOnboardingUseCase {
        return GetOnboardingUseCase(repository)
    }

}
