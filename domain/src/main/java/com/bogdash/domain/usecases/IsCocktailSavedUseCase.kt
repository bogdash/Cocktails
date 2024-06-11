package com.bogdash.domain.usecases

import com.bogdash.domain.repository.CocktailRepository

class IsCocktailSavedUseCase(private val repository: CocktailRepository) {

    suspend fun execute(id: String): Boolean {
        return repository.isCocktailByIdSaved(id)
    }

}
