package com.bogdash.data.storage.database.converters

import androidx.room.TypeConverter
import com.bogdash.data.storage.database.entities.IngredientEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromIngredientList(value: String): List<IngredientEntity> {
        val listType = object : TypeToken<List<IngredientEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<IngredientEntity>):String {
        return Gson().toJson(list)
    }

}