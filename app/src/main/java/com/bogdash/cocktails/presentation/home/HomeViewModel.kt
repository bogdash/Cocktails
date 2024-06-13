package com.bogdash.cocktails.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val allCocktails = mutableListOf<Drink>()
    private var currentPage = 0
    private val pageSize = 10

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

    private fun resetCocktails() {
        allCocktails.clear()
    }
}
