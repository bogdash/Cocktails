package com.bogdash.domain.usecases

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository

class GetSavedCocktailDetailsByIdUseCase (private val repository: CocktailRepository) {
    suspend fun execute(id: String): Cocktails {
        return repository.getCocktailById(id)
    }
}