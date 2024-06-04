package com.bogdash.data.repository

import com.bogdash.data.storage.network.ConstantsForNetwork.DATE_FORMAT_PATTERN
import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.data.storage.preferences.CocktailPreferences
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CocktailRepositoryImplementation(
    private val cocktailApiService: CocktailsApiService,
    private val cocktailPreferences: CocktailPreferences
) : CocktailRepository {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    override suspend fun getCocktailOfTheDay(): Cocktails {
        return withContext(Dispatchers.IO) {
            val today = dateFormat.format(Date())

            val lastUpdateDate = cocktailPreferences.getLastUpdateDate()
            val savedDrink = cocktailPreferences.getCocktail()

            if (lastUpdateDate == today && savedDrink != null) {
                Cocktails(listOf(savedDrink))
            } else {
                val dataCocktails = cocktailApiService.getCocktailOfTheDay()
                val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })

                val drink = dataCocktails.drinks.firstOrNull()
                if (drink != null) {
                    cocktailPreferences.saveCocktail(drink.toDomain())
                    cocktailPreferences.saveLastUpdateDate(today)
                }

                domainCocktails
            }
        }
    }

}
