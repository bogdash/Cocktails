package com.bogdash.domain.usecases

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository

class GetCocktailsByPageUseCase(private val repository: CocktailRepository) {

    suspend fun execute(): Cocktails {
        return repository.getCocktailsByPage()
    }
}
