package com.bogdash.data.repository

import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailRepositoryImplementation(
    private val cocktailApiService: CocktailsApiService
) : CocktailRepository {

    override suspend fun getFilteredCocktailsByAlcoholType(type: String): Cocktails {
        return withContext(Dispatchers.IO) {
            val dataCocktails = cocktailApiService.getFilteredCocktailsByAlcoholType(type)
            val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })
            domainCocktails
        }
    }
}
