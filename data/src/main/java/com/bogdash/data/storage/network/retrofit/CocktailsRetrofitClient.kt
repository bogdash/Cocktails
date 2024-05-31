package com.bogdash.data.storage.network.retrofit

import com.bogdash.data.storage.network.ConstantsForNetwork.BASE_URL
import com.bogdash.data.storage.network.okhttp.CocktailsOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CocktailsRetrofitClient {
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
