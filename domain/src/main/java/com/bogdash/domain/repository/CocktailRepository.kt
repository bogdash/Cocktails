package com.bogdash.domain.repository

import com.bogdash.domain.models.Cocktails

interface CocktailRepository {
    suspend fun getCocktailOfTheDay(): Cocktails
    suspend fun getFilteredCocktailsByAlcoholType(type: String): Cocktails
}
