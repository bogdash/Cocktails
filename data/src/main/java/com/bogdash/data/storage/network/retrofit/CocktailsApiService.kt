package com.bogdash.data.storage.network.retrofit
import retrofit2.http.GET
import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.Query

import com.bogdash.data.storage.network.ConstantsForNetwork.ID
import com.bogdash.data.storage.network.ConstantsForNetwork.LOOKUP
import com.bogdash.data.storage.network.ConstantsForNetwork.RANDOM

interface CocktailsApiService {
    @GET(RANDOM)
    suspend fun getCocktailOfTheDay(): CocktailsDto

    @GET(LOOKUP)
    suspend fun getCocktailDetailsById(@Query(ID) id: String): CocktailsDto
    @GET("search.php")
    suspend fun searchCocktailsByName(@Query("s") name: String): CocktailsDto
}
