package com.bogdash.cocktails.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.Constants.Filters.DEFAULT_FILTER
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

    private val _alcoholicFilterType = MutableLiveData<String>()
    val alcoholicFilterType: MutableLiveData<String> = _alcoholicFilterType

    private val _ingredientsFilterType = MutableLiveData<List<String>>()
    val ingredientsFilterType: LiveData<List<String>> = _ingredientsFilterType



    var isAlcoholFilterApplied = true
        private set

    private var scrollPosition = 0
    private var scrollOffset = 0
    private val allCocktails = mutableListOf<Drink>()
    private var currentPage = 0
    private var isFilterChanged = true

    init {
        loadInitialCocktails()
    }

    fun setAlcoholicFilterType(type: String) {
        isAlcoholFilterApplied = true
        isFilterChanged = true
        _alcoholicFilterType.value = type
    }

    fun setIngredientsFilter(ingredients: List<String>) {
        isAlcoholFilterApplied = false
        isFilterChanged = true
        _ingredientsFilterType.value = ingredients
    }

    fun setDefaultFilterType() {
        isAlcoholFilterApplied = true
        _alcoholicFilterType.value = DEFAULT_FILTER
    }

    fun getFilteredCocktailsByAlcoholType(type: String) {
        if (isAlcoholFilterApplied && isFilterChanged) {
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

    }

    fun getFilteredCocktailsByIngredients(ingredients: List<String>) {
        if (!isAlcoholFilterApplied && isFilterChanged) {
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

    }

    fun getNextPageCocktails() {
        viewModelScope.launch {
            try {
                val nextPageCocktails = allCocktails.take((currentPage + 1) * PAGE_SIZE)
                cocktailsMutable.value = Cocktails(nextPageCocktails)
                currentPage++
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_loading_cocktails)
            }
        }
    }

    fun getIsFilterChanged(): Boolean {
        return isFilterChanged
    }

    fun setIsFilterChanged(isFilterChanged: Boolean) {
        this.isFilterChanged = isFilterChanged
    }

    fun setScrollPosition(scrollPosition: Int) {
        this.scrollPosition = scrollPosition
    }

    fun getScrollPosition(): Int {
        return scrollPosition
    }

    fun setScrollOffset(scrollOffset: Int) {
        this.scrollOffset = scrollOffset
    }

    fun getScrollOffset(): Int {
        return scrollOffset
    }

    private fun loadInitialCocktails() {
        getFilteredCocktailsByAlcoholType(DEFAULT_FILTER)
    }

    private fun resetCocktails() {
        allCocktails.clear()
        currentPage = 0
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
