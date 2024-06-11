package com.bogdash.data.storage.network.dto

import com.bogdash.domain.models.Drink
import com.bogdash.domain.models.Ingredient

class DrinkDto(
    private val idDrink: String,
    private val strDrink: String,
    private val strTags: String,
    private val strCategory: String,
    private val strIBA: String,
    private val strAlcoholic: String,
    private val strGlass: String,
    private val strInstructions: String,
    private val strDrinkThumb: String,
    private val strIngredient1: String?,
    private val strIngredient2: String?,
    private val strIngredient3: String?,
    private val strIngredient4: String?,
    private val strIngredient5: String?,
    private val strIngredient6: String?,
    private val strIngredient7: String?,
    private val strIngredient8: String?,
    private val strIngredient9: String?,
    private val strIngredient10: String?,
    private val strIngredient11: String?,
    private val strIngredient12: String?,
    private val strIngredient13: String?,
    private val strIngredient14: String?,
    private val strIngredient15: String?,
    private val strMeasure1: String?,
    private val strMeasure2: String?,
    private val strMeasure3: String?,
    private val strMeasure4: String?,
    private val strMeasure5: String?,
    private val strMeasure6: String?,
    private val strMeasure7: String?,
    private val strMeasure8: String?,
    private val strMeasure9: String?,
    private val strMeasure10: String?,
    private val strMeasure11: String?,
    private val strMeasure12: String?,
    private val strMeasure13: String?,
    private val strMeasure14: String?,
    private val strMeasure15: String?,
    private val strCreativeCommonsConfirmed: String?,
    private val dateModified: String
) {

    fun toDomain(): Drink {
        return Drink(
            id = idDrink,
            name = strDrink,
            tags = strTags,
            category = strCategory,
            iba = strIBA,
            alcoholic = strAlcoholic,
            glass = strGlass,
            instructions = strInstructions,
            thumb = strDrinkThumb,
            ingredients = prepareIngredients(),
            creativeCommonsConfirmed = strCreativeCommonsConfirmed,
            dateModified = dateModified
        )
    }

    private fun prepareIngredients(): List<Ingredient> =
        listOfNotNull(
            strIngredient1?.let { Ingredient(it, strMeasure1) },
            strIngredient2?.let { Ingredient(it, strMeasure2) },
            strIngredient3?.let { Ingredient(it, strMeasure3) },
            strIngredient4?.let { Ingredient(it, strMeasure4) },
            strIngredient5?.let { Ingredient(it, strMeasure5) },
            strIngredient6?.let { Ingredient(it, strMeasure6) },
            strIngredient7?.let { Ingredient(it, strMeasure7) },
            strIngredient8?.let { Ingredient(it, strMeasure8) },
            strIngredient9?.let { Ingredient(it, strMeasure9) },
            strIngredient10?.let { Ingredient(it, strMeasure10) },
            strIngredient11?.let { Ingredient(it, strMeasure11) },
            strIngredient12?.let { Ingredient(it, strMeasure12) },
            strIngredient13?.let { Ingredient(it, strMeasure13) },
            strIngredient14?.let { Ingredient(it, strMeasure14) },
            strIngredient15?.let { Ingredient(it, strMeasure15) }
        ).filter { it.name.isNotEmpty() }

}
