package com.bogdash.domain.usecases

import com.bogdash.domain.models.CocktailsWithCategory
import com.bogdash.domain.repository.CocktailRepository

class GetSavedCocktailsUseCase (private val repository: CocktailRepository) {
    suspend fun execute(): List<CocktailsWithCategory> {
        return repository.getCocktailsWithCategories()
    }
}