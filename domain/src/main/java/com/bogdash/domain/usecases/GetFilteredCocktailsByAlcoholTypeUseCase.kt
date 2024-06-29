package com.bogdash.domain.usecases

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository

class GetFilteredCocktailsByAlcoholTypeUseCase(private val repository: CocktailRepository) {

    suspend fun execute(type: String): Cocktails {
        return repository.getFilteredCocktailsByAlcoholType(type)
    }

}
