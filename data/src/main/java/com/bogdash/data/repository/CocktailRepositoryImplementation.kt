package com.bogdash.data.repository

import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailRepositoryImplementation(
    private val cocktailApiService: CocktailsApiService
) : CocktailRepository {

    override suspend fun getCocktailsByPage(): Cocktails {
        return withContext(Dispatchers.IO) {
            val dataCocktails = cocktailApiService.getCocktailByPage()
            val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toData().toDomain() })
            domainCocktails
        }
    }
}
