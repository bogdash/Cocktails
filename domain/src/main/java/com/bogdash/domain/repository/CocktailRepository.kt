package com.bogdash.domain.repository

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink

interface CocktailRepository {
    suspend fun getCocktailOfTheDay(): Cocktails
    suspend fun getCocktailDetailsById(id: String): Cocktails
    suspend fun saveCocktail(drink: Drink)
}
