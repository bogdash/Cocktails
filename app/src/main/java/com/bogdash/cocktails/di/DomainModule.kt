package com.bogdash.cocktails.di

import com.bogdash.domain.repository.CocktailRepository
import com.bogdash.domain.usecases.DeleteCocktailByIdUseCase
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.GetCocktailOfTheDayUseCase
import com.bogdash.domain.usecases.IsCocktailSavedUseCase
import com.bogdash.domain.usecases.SaveCocktailByIdUseCase
import com.bogdash.domain.repository.OnboardingRepository
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

    @Provides
    fun provideGetCocktailDetailsByIdUseCase(repository: CocktailRepository): GetCocktailDetailsByIdUseCase {
        return GetCocktailDetailsByIdUseCase(repository)
    }

    @Provides
    fun provideSaveCocktailById(repository: CocktailRepository): SaveCocktailByIdUseCase {
        return SaveCocktailByIdUseCase(repository)
    }

    @Provides
    fun provideDeleteCocktailById(repository: CocktailRepository): DeleteCocktailByIdUseCase {
        return DeleteCocktailByIdUseCase(repository)
    }

    @Provides
    fun provideIsCocktailSaved(repository: CocktailRepository): IsCocktailSavedUseCase {
        return IsCocktailSavedUseCase(repository)
    }

}
