package com.bogdash.cocktails.di

import android.content.Context
import com.bogdash.data.repository.CocktailRepositoryImplementation
import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.data.storage.network.retrofit.CocktailsRetrofitClient
import com.bogdash.data.storage.preferences.CocktailPreferences
import com.bogdash.data.storage.preferences.CocktailPreferencesImplementation
import com.bogdash.domain.repository.CocktailRepository
import android.content.SharedPreferences
import com.bogdash.cocktails.Constants.Data.APP_PREFS
import com.bogdash.data.repository.OnboardingRepositoryImplementation
import com.bogdash.domain.repository.OnboardingRepository
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


    fun provideRetrofit(): Retrofit {
        return CocktailsRetrofitClient.client
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideCocktailApiService(retrofit: Retrofit): CocktailsApiService {
        return retrofit.create(CocktailsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCocktailPreferences(@ApplicationContext context: Context): CocktailPreferences {
        return CocktailPreferencesImplementation(context)
    }

    @Provides
    @Singleton
    fun provideCocktailRepository(
        apiService: CocktailsApiService,
        cocktailPreferences: CocktailPreferences
    ): CocktailRepository {
        return CocktailRepositoryImplementation(apiService, cocktailPreferences)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(sharedPreferences: SharedPreferences): OnboardingRepository {
        return OnboardingRepositoryImplementation(sharedPreferences)
    }

}
