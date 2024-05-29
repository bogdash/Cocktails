package com.bogdash.domain.models

data class Drink (
    val id: String,
    val name: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val thumb: String,
    val ingredients: List<String>,
    val measures: List<String>
)