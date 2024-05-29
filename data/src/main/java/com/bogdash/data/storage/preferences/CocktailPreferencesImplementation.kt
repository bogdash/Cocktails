package com.bogdash.data.storage.preferences

import android.content.Context
import android.content.SharedPreferences
import com.bogdash.data.storage.models.Drink
import com.bogdash.data.storage.preferences.ConstantsForSharedPreferences.COCKTAIL_PREFS
import com.bogdash.data.storage.preferences.ConstantsForSharedPreferences.LAST_UPDATE_DATE
import com.bogdash.data.storage.preferences.ConstantsForSharedPreferences.SAVED_COCKTAIL
import com.google.gson.Gson

class CocktailPreferencesImplementation(context: Context) : CocktailPreferences {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(COCKTAIL_PREFS, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun saveCocktail(drink: Drink) {
        val drinkJson = gson.toJson(drink)
        sharedPreferences.edit().putString(SAVED_COCKTAIL, drinkJson).apply()
    }

    override fun getCocktail(): Drink? {
        val drinkJson = sharedPreferences.getString(SAVED_COCKTAIL, null)
        return drinkJson?.let { gson.fromJson(it, Drink::class.java) }
    }

    override fun saveLastUpdateDate(date: String) {
        sharedPreferences.edit().putString(LAST_UPDATE_DATE, date).apply()
    }

    override fun getLastUpdateDate(): String? {
        return sharedPreferences.getString(LAST_UPDATE_DATE, null)
    }
}