package com.bogdash.cocktails.di

import com.bogdash.domain.repository.CocktailRepository
import com.bogdash.domain.usecases.DeleteCocktailByIdUseCase
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.GetCocktailOfTheDayUseCase
import com.bogdash.domain.usecases.IsCocktailSavedUseCase
import com.bogdash.domain.usecases.SaveCocktailByIdUseCase
import com.bogdash.domain.repository.OnboardingRepository
import com.bogdash.domain.usecases.GetSavedCocktailsUseCase
import com.bogdash.domain.usecases.GetOnboardingUseCase
import com.bogdash.domain.usecases.GetFilteredCocktailsByAlcoholTypeUseCase
import com.bogdash.domain.usecases.GetSavedCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.SearchCocktailsByNameUseCase
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
    fun provideSaveCocktailByIdUseCase(repository: CocktailRepository): SaveCocktailByIdUseCase {
        return SaveCocktailByIdUseCase(repository)
    }

    @Provides
    fun provideDeleteCocktailByIdUseCase(repository: CocktailRepository): DeleteCocktailByIdUseCase {
        return DeleteCocktailByIdUseCase(repository)
    }

    @Provides
    fun provideIsCocktailSavedUseCase(repository: CocktailRepository): IsCocktailSavedUseCase {
        return IsCocktailSavedUseCase(repository)
    }

    @Provides
    fun provideGetFilteredCocktailsByAlcoholTypeUseCase(repository: CocktailRepository): GetFilteredCocktailsByAlcoholTypeUseCase {
        return GetFilteredCocktailsByAlcoholTypeUseCase(repository)
    }

    @Provides
    fun provideSearchCocktailByNameUseCase(repository: CocktailRepository): SearchCocktailsByNameUseCase {
        return SearchCocktailsByNameUseCase(repository)
    }

    @Provides
    fun provideGetSavedCocktailsUseCase(repository: CocktailRepository): GetSavedCocktailsUseCase {
        return GetSavedCocktailsUseCase(repository)
    }

    @Provides
    fun provideGetSavedCocktailDetailsByIdUseCase(repository: CocktailRepository): GetSavedCocktailDetailsByIdUseCase {
        return GetSavedCocktailDetailsByIdUseCase(repository)
    }
}
