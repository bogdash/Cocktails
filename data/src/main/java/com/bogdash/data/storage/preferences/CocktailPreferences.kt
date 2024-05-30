package com.bogdash.data.storage.preferences

import com.bogdash.data.storage.models.Drink
import com.bogdash.data.storage.network.dto.DrinkDto

interface CocktailPreferences {
    fun saveCocktail(drink: Drink)
    fun getCocktail(): Drink?
    fun saveLastUpdateDate(date: String)
    fun getLastUpdateDate(): String?
}