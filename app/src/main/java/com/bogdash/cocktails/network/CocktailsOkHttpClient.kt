package com.bogdash.cocktails.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CocktailsOkHttpClient {
    val client: OkHttpClient = getHttpClient()

    private fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
