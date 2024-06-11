package com.bogdash.cocktails.presentation.detail.models.mappers

import com.bogdash.domain.models.Ingredient
import com.bogdash.cocktails.presentation.detail.models.ParcelableIngredient

fun Ingredient.toParcelable(): ParcelableIngredient {
    return ParcelableIngredient(
        name = this.name,
        measure = this.measure
    )
}

fun List<Ingredient>.toParcelable(): List<ParcelableIngredient> {
    return this.map { it.toParcelable() }
}
