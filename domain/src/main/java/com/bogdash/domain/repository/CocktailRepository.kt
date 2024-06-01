package com.bogdash.domain.repository

import com.bogdash.domain.models.Cocktails

interface CocktailRepository {
    suspend fun searchCocktailsByName(name: String): Cocktails
}
