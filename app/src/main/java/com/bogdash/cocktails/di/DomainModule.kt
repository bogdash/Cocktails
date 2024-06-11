package com.bogdash.cocktails.di

import com.bogdash.domain.repository.CocktailRepository
import com.bogdash.domain.repository.OnboardingRepository
import com.bogdash.domain.usecases.GetCocktailOfTheDayUseCase
import com.bogdash.domain.usecases.GetOnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideGetCocktailOfTheDayUseCase(repository: CocktailRepository): GetCocktailOfTheDayUseCase {
        return GetCocktailOfTheDayUseCase(repository)
    }

    @Provides
    fun provideGetOnboardingUseCase(repository: OnboardingRepository): GetOnboardingUseCase {
        return GetOnboardingUseCase(repository)
    }
}
