package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApiService {

    @GET("filter.php?a=Alcoholic")
    suspend fun getCocktailByPage(): CocktailsDto
}
