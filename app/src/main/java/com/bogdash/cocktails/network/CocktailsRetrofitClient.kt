package com.bogdash.cocktails.network

import com.bogdash.cocktails.Constants.Network.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CocktailsRetrofitClient {
    val client: Retrofit = getRetrofitClient()

    private fun getRetrofitClient(): Retrofit {
        val cocktailsOkHttpClient = CocktailsOkHttpClient()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cocktailsOkHttpClient.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
