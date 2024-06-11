package com.bogdash.cocktails.presentation.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogdash.cocktails.Constants.Filters.DEFAULT_FILTER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor() : ViewModel() {

    private val _alcoholicFilterType = MutableLiveData<String>()
    val alcoholicFilterType: LiveData<String> = _alcoholicFilterType

    private val _ingredientsFilterType = MutableLiveData<List<String>>()
    val ingredientsFilterType: LiveData<List<String>> = _ingredientsFilterType

    fun setAlcoholicFilterType(type: String) {
        _alcoholicFilterType.value = type
    }

    fun setIngredientsFilter(ingredients: List<String>) {
        _ingredientsFilterType.value = ingredients
    }

    fun resetIngredientsFilter() {
        _ingredientsFilterType.value = emptyList()
    }

    fun setDefaultFilterType() {
        _alcoholicFilterType.value = DEFAULT_FILTER
    }
}