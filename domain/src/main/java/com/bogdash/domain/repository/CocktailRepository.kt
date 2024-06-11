package com.bogdash.domain.repository

import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink

interface CocktailRepository {
    suspend fun getCocktailOfTheDay(): Cocktails
    suspend fun getCocktailDetailsById(id: String): Cocktails
    suspend fun saveCocktail(drink: Drink)
    suspend fun getCocktailById(id: String): Drink
    suspend fun isCocktailByIdSaved(id: String): Boolean
    suspend fun deleteCocktailById(id: String)
    suspend fun getFilteredCocktailsByAlcoholType(type: String): Cocktails
    suspend fun getFilteredCocktailsByIngredient(ingredients: List<String>) : Cocktails
}
