package com.bogdash.data.mappers

import com.bogdash.data.storage.database.entities.DrinkEntity
import com.bogdash.data.storage.database.entities.IngredientEntity
import com.bogdash.domain.models.Drink
import com.bogdash.domain.models.Ingredient

internal object DrinkMapper {

    fun toEntity(drink: Drink): DrinkEntity {
        return DrinkEntity(
            id = drink.id,
            name = drink.name,
            tags = drink.tags,
            category = drink.category,
            iba = drink.iba,
            alcoholic = drink.alcoholic,
            glass = drink.glass,
            instructions = drink.instructions,
            thumb = drink.thumb,
            creativeCommonsConfirmed = drink.creativeCommonsConfirmed,
            dateModified = drink.dateModified,
            isFavorite = drink.isFavorite
        )
    }

    fun fromEntity(drinkEntity: DrinkEntity, ingredientEntities: List<IngredientEntity>): Drink {
        return Drink(
            id = drinkEntity.id,
            name = drinkEntity.name ?: "",
            tags = drinkEntity.tags,
            category = drinkEntity.category ?: "",
            iba = drinkEntity.iba,
            alcoholic = drinkEntity.alcoholic ?: "",
            glass = drinkEntity.glass ?: "",
            instructions = drinkEntity.instructions ?: "",
            thumb = drinkEntity.thumb ?: "",
            ingredients = ingredientEntities.map { Ingredient(it.name, it.measure) },
            creativeCommonsConfirmed = drinkEntity.creativeCommonsConfirmed,
            dateModified = drinkEntity.dateModified ?: "",
            isFavorite = drinkEntity.isFavorite
        )
    }

}
