package com.bogdash.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdash.data.storage.database.entities.IngredientEntity

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient WHERE drinkId = :drinkId")
    suspend fun getIngredientsByDrinkId(drinkId: String): List<IngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("DELETE FROM ingredient WHERE drinkId = :drinkId")
    suspend fun deleteIngredientsByDrinkId(drinkId: String)
}