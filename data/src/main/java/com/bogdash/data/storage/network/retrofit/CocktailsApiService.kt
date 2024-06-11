package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.ConstantsForNetwork.RANDOM
import com.bogdash.data.storage.network.ConstantsForNetwork.ALCOHOLIC_PARAMETER
import com.bogdash.data.storage.network.ConstantsForNetwork.FILTER
import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.GET
import retrofit2.http.Query
interface CocktailsApiService {
    @GET(RANDOM)
    suspend fun getCocktailOfTheDay(): CocktailsDto

    @GET(FILTER)
    suspend fun getFilteredCocktailsByAlcoholType(@Query(ALCOHOLIC_PARAMETER) type: String): CocktailsDto
}
