package com.bogdash.cocktails.presentation.detail.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableIngredient(
    val name: String,
    val measure: String?
) : Parcelable

@Parcelize
data class ParcelableDrink(
    val id: String,
    val name: String,
    val tags: String?,
    val category: String,
    val iba: String?,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val thumb: String,
    val ingredients: List<ParcelableIngredient>,
    val creativeCommonsConfirmed: String?,
    val dateModified: String
) : Parcelable