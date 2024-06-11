package com.bogdash.data.storage.network.retrofit
import com.bogdash.data.storage.network.ConstantsForNetwork.SEARCH
import com.bogdash.data.storage.network.ConstantsForNetwork.SEARCH_QUERY
import retrofit2.http.GET
import com.bogdash.data.storage.network.dto.CocktailsDto
import retrofit2.http.Query

import com.bogdash.data.storage.network.ConstantsForNetwork.ID
import com.bogdash.data.storage.network.ConstantsForNetwork.LOOKUP
import com.bogdash.data.storage.network.ConstantsForNetwork.RANDOM
import com.bogdash.data.storage.network.ConstantsForNetwork.ALCOHOLIC_PARAMETER
import com.bogdash.data.storage.network.ConstantsForNetwork.FILTER

interface CocktailsApiService {
    @GET(RANDOM)
    suspend fun getCocktailOfTheDay(): CocktailsDto

    @GET(LOOKUP)
    suspend fun getCocktailDetailsById(@Query(ID) id: String): CocktailsDto
    @GET(SEARCH)
    suspend fun searchCocktailsByName(@Query(SEARCH_QUERY) name: String): CocktailsDto
    
    @GET(FILTER)
    suspend fun getFilteredCocktailsByAlcoholType(@Query(ALCOHOLIC_PARAMETER) type: String): CocktailsDto
}
