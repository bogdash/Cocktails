package com.bogdash.data.storage.network.retrofit
import retrofit2.http.GET
import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.Query

interface CocktailsApiService {
    @GET("search.php")
    suspend fun searchCocktailsByName(@Query("s") name: String): CocktailsDto
}
