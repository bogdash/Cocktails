package com.bogdash.data.mappers

import com.bogdash.data.storage.database.entities.IngredientEntity
import com.bogdash.domain.models.Ingredient

object IngredientMapper {

    fun toEntity(ingredient: Ingredient, drinkId: String): IngredientEntity {
        return IngredientEntity(
            name = ingredient.name,
            measure = ingredient.measure ?: "",
            drinkId = drinkId
        )
    }

}
