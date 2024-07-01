package com.bogdash.domain.usecases

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository

class GetFilteredCocktailsByIngredientUseCase(private val repository: CocktailRepository) {

    suspend fun execute(ingredients: List<String>): Cocktails {
        return repository.getFilteredCocktailsByIngredient(ingredients)
    }

}
