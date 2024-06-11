package com.bogdash.cocktails.di

import android.content.Context
import androidx.room.Room
import com.bogdash.data.repository.CocktailRepositoryImplementation
import com.bogdash.data.storage.database.AppDatabase
import com.bogdash.data.storage.database.dao.DrinkDao
import com.bogdash.data.storage.database.dao.IngredientDao
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

    @Provides
    @Singleton
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cocktails_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDrinkDao(database: AppDatabase): DrinkDao {
        return database.drinkDao()
    }

    @Provides
    @Singleton
    fun provideIngredientDao(database: AppDatabase): IngredientDao {
        return database.ingredientDao()
    }

    @Provides
    @Singleton
    fun provideCocktailRepository(
        apiService: CocktailsApiService,
        cocktailPreferences: CocktailPreferences,
        drinkDao: DrinkDao,
        ingredientDao: IngredientDao
    ): CocktailRepository {
        return CocktailRepositoryImplementation(apiService, cocktailPreferences, drinkDao, ingredientDao)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(sharedPreferences: SharedPreferences): OnboardingRepository {
        return OnboardingRepositoryImplementation(sharedPreferences)
    }

}
