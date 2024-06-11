package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.ConstantsForNetwork.ID
import com.bogdash.data.storage.network.ConstantsForNetwork.LOOKUP
import com.bogdash.data.storage.network.ConstantsForNetwork.RANDOM
import com.bogdash.data.storage.network.ConstantsForNetwork.ALCOHOLIC_PARAMETER
import com.bogdash.data.storage.network.ConstantsForNetwork.FILTER
import com.bogdash.data.storage.network.ConstantsForNetwork.INGREDIENT_PARAMETER
import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApiService {
    @GET(RANDOM)
    suspend fun getCocktailOfTheDay(): CocktailsDto

    @GET(LOOKUP)
    suspend fun getCocktailDetailsById(@Query(ID) id: String): CocktailsDto
    
    @GET(FILTER)
    suspend fun getFilteredCocktailsByAlcoholType(@Query(ALCOHOLIC_PARAMETER) type: String): CocktailsDto

    @GET(FILTER)
    suspend fun getFilteredCocktailsByIngredient(@Query(value = "") query: String) : CocktailsDto
}
