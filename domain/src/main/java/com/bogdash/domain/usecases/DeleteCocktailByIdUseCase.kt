package com.bogdash.domain.usecases

import com.bogdash.domain.models.Drink
import com.bogdash.domain.repository.CocktailRepository

class DeleteCocktailByIdUseCase(
    private val repository: CocktailRepository
) {

    suspend fun execute(drink: Drink) {
        repository.deleteCocktailById(drink.id)
    }

}
