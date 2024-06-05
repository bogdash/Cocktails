package com.bogdash.cocktails.di

import com.bogdash.domain.repository.CocktailRepository
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.GetCocktailsByPageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCocktailsByPage(repository: CocktailRepository): GetCocktailsByPageUseCase {
        return GetCocktailsByPageUseCase(repository)
    }

    @Provides
    fun provideGetCocktailDetailsById(repository: CocktailRepository): GetCocktailDetailsByIdUseCase {
        return GetCocktailDetailsByIdUseCase(repository)
    }
}
