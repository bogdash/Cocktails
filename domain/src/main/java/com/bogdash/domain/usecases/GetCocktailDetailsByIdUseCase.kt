package com.bogdash.domain.usecases

import com.bogdash.domain.models.Drink
import com.bogdash.domain.repository.CocktailRepository

class GetCocktailDetailsByIdUseCase(private val repository: CocktailRepository) {
    suspend fun execute(id: String): Drink {
        return repository.getCocktailDetailsById(id)
    }
}
