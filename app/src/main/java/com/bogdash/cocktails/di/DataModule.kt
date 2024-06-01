package com.bogdash.cocktails.di

import com.bogdash.data.repository.CocktailRepositoryImplementation
import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.data.storage.network.retrofit.CocktailsRetrofitClient
import com.bogdash.domain.repository.CocktailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return CocktailsRetrofitClient.client
    }

    @Provides
    @Singleton
    fun provideCocktailApiService(retrofit: Retrofit): CocktailsApiService {
        return retrofit.create(CocktailsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCocktailRepository(
        apiService: CocktailsApiService
    ): CocktailRepository {
        return CocktailRepositoryImplementation(apiService)
    }
}
