package com.bogdash.domain.usecases

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository
class SearchCocktailsByNameUseCase (private val repository: CocktailRepository) {
    suspend fun execute(name: String): Cocktails {
        return repository.searchCocktailsByName(name)
    }
}
