package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.GET

interface CocktailsApiService {
    @GET("random.php")
    suspend fun getCocktailOfTheDay(): CocktailsDto
}
