package com.bogdash.cocktails.di

import com.bogdash.domain.repository.CocktailRepository
import com.bogdash.domain.usecases.GetFilteredCocktailsByAlcoholTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetFilteredCocktailsByAlcoholType(repository: CocktailRepository): GetFilteredCocktailsByAlcoholTypeUseCase {
        return GetFilteredCocktailsByAlcoholTypeUseCase(repository)
    }
}
