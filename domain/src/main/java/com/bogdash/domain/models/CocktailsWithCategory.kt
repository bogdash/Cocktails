package com.bogdash.domain.models

data class CocktailsWithCategory(
    val category: String,
    val cocktails: Cocktails
)

fun toCocktailsWithCategory(category: String, cocktails: Cocktails): CocktailsWithCategory{
    return CocktailsWithCategory(category,cocktails)
}

