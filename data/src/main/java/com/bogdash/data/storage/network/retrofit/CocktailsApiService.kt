package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.GET

interface CocktailsApiService {

    @GET("search.php?f=a")
    suspend fun getCocktailByPage(): CocktailsDto
}
