package com.bogdash.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Drink(
    val id: String,
    val name: String,
    val tags: String?,
    val category: String?,
    val iba: String?,
    val alcoholic: String?,
    val glass: String?,
    val instructions: String?,
    val thumb: String?,
    val ingredients: List<Ingredient>,
    val creativeCommonsConfirmed: String?,
    val dateModified: String?,
    var isFavorite: Boolean = false
)

@Serializable
data class Ingredient(
    val name: String,
    val measure: String?
)
