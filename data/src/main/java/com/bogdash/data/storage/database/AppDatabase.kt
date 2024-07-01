package com.bogdash.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bogdash.data.storage.database.dao.DrinkDao
import com.bogdash.data.storage.database.dao.IngredientDao
import com.bogdash.data.storage.database.entities.DrinkEntity
import com.bogdash.data.storage.database.entities.IngredientEntity

@Database(entities = [DrinkEntity::class, IngredientEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun drinkDao(): DrinkDao
    abstract fun ingredientDao(): IngredientDao

}
