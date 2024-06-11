package com.bogdash.data.storage.preferences

import com.bogdash.domain.models.Drink

interface CocktailPreferences {
    fun saveCocktail(drink: Drink)
    fun getCocktail(): Drink?
    fun saveLastUpdateDate(date: String)
    fun getLastUpdateDate(): String?
}