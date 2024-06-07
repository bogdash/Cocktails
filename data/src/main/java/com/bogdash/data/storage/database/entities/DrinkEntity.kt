package com.bogdash.data.storage.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "drink")
data class DrinkEntity (
    @PrimaryKey val id: String,
    val name: String?,
    val tags: String?,
    val category: String?,
    val iba: String?,
    val alcoholic: String?,
    val glass: String?,
    val instructions: String?,
    val thumb: String?,
    val creativeCommonsConfirmed: String?,
    val dateModified: String?,
    val isFavorite: Boolean = false
)

@Entity(
    tableName = "ingredient",
    foreignKeys = [ForeignKey(
        entity = DrinkEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("drinkId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class IngredientEntity(
    @PrimaryKey val name: String,
    val measure: String,
    val drinkId: String
)
