package com.bogdash.cocktails.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.Constants
import com.bogdash.cocktails.R
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.GetFilteredCocktailsByAlcoholTypeUseCase
import com.bogdash.domain.usecases.GetFilteredCocktailsByIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredCocktailsByAlcoholTypeUseCase: GetFilteredCocktailsByAlcoholTypeUseCase,
    private val getFilteredCocktailsByIngredientUseCase: GetFilteredCocktailsByIngredientUseCase
) : ViewModel() {

    private val cocktailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = cocktailsMutable

    private val loadingMutable = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loadingMutable

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    private val _alcoholicFilterType = MutableLiveData<String?>()
    val alcoholicFilterType: MutableLiveData<String?> = _alcoholicFilterType

    private val _ingredientsFilterType = MutableLiveData<List<String>>()
    val ingredientsFilterType: LiveData<List<String>> = _ingredientsFilterType

    private val allCocktails = mutableListOf<Drink>()
    private var currentPage = 0
    private val pageSize = 10

    init {
        setDefaultFilterType()
        Log.d("FiltersViewModel", "Вью модель создалась")
    }

    fun setAlcoholicFilterType(type: String?) {
        _alcoholicFilterType.value = type
    }

    fun setIngredientsFilter(ingredients: List<String>) {
        _ingredientsFilterType.value = ingredients
    }

    fun resetIngredientsFilter() {
        _ingredientsFilterType.value = emptyList()
    }

    fun setDefaultFilterType() {
        _alcoholicFilterType.value = Constants.Filters.DEFAULT_FILTER
    }

    fun getFilteredCocktailsByAlcoholType(type: String) {
        resetCocktails()
        viewModelScope.launch {
            try {
                loadingMutable.value = true
                val cocktails = getFilteredCocktailsByAlcoholTypeUseCase.execute(type)
                allCocktails.addAll(cocktails.drinks)
                getNextPageCocktails()
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_loading_cocktails)
            } finally {
                loadingMutable.value = false
            }
        }
    }

    fun getFilteredCocktailsByIngredients(ingredients: List<String>) {
        resetCocktails()
        viewModelScope.launch {
            try {
                loadingMutable.value = true
                val cocktails = getFilteredCocktailsByIngredientUseCase.execute(ingredients)
                allCocktails.addAll(cocktails.drinks)
                getNextPageCocktails()
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_loading_cocktails)
            } finally {
                loadingMutable.value = false
            }
        }
    }

    fun getNextPageCocktails() {
        viewModelScope.launch {
            try {
                val nextPageCocktails = allCocktails.take((currentPage + 1) * pageSize)
                cocktailsMutable.value = Cocktails(nextPageCocktails)
                currentPage++
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_loading_cocktails)
            }
        }
    }

    fun loadInitialCocktails() {
        _ingredientsFilterType.value?.let { filterType ->
            getFilteredCocktailsByIngredients(filterType)
            return
        }
        _alcoholicFilterType.value?.let {  filterType ->
            getFilteredCocktailsByAlcoholType(filterType)
        }
    }

    private fun resetCocktails() {
        allCocktails.clear()
    }
}
