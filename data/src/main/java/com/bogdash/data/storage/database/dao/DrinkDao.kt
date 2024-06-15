package com.bogdash.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdash.data.storage.database.entities.DrinkEntity

@Dao
interface DrinkDao {
    @Query("SELECT DISTINCT category FROM drink")
    suspend fun getCocktailCategories(): List<String>

    @Query("SELECT * FROM drink")
    suspend fun getAllDrinks() : List<DrinkEntity>

    @Query("SELECT * FROM drink WHERE category = :category")
    suspend fun getDrinksByCategory(category: String) : List<DrinkEntity>

    @Query("SELECT * FROM drink WHERE id = :id")
    suspend fun getDrinkById(id: String) : DrinkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: DrinkEntity)

    @Query("DELETE FROM drink WHERE id = :id")
    suspend fun deleteDrinkById(id: String)
}