package com.bogdash.data.storage.network.models

data class Drink(
    val id: String,
    val name: String,
    val tags: String?,
    val category: String,
    val iba: String?,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val thumb: String,
    val ingredients: List<Ingredient>,
    val creativeCommonsConfirmed: String?,
    val dateModified: String?
){
    fun toDomain(): com.bogdash.domain.models.Drink {
        val ingredients = ingredients.map {
            com.bogdash.domain.models.Ingredient(
                it.name,
                it.measure
            )
        }

        return com.bogdash.domain.models.Drink(
            id,
            name,
            category,
            alcoholic,
            glass,
            instructions,
            thumb,
            ingredients
        )
    }
}

data class Ingredient(
    val name: String,
    val measure: String?
)
