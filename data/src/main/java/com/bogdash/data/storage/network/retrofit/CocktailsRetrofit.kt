package com.bogdash.data.storage.network.retrofit

class CocktailsRetrofit {
    private val cocktailsApiService = CocktailsRetrofitClient.client.create(
        CocktailsApiService::class.java
    )
}
