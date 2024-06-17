package com.bogdash.data.storage.network.retrofit

import android.app.Application
import com.bogdash.data.storage.network.ConstantsForNetwork.BASE_URL
import com.bogdash.data.storage.network.okhttp.CocktailsOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CocktailsRetrofitClient {
    lateinit var client: Retrofit
        private set

    fun initialize(application: Application) {
        val cocktailsOkHttpClient = CocktailsOkHttpClient(application)
        client = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cocktailsOkHttpClient.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

