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

    private val _uiState = MutableLiveData(
        UIState(
            scrollPosition = 0,
            scrollOffset = 0,
            currentPage = 0
        )
    )
    val uiState: LiveData<UIState> = _uiState

    private val _isFilterChanged = MutableLiveData(true)
    val isFilterChanged: LiveData<Boolean> = _isFilterChanged

    private val _isAlcoholFilterApplied = MutableLiveData(true)
    val isAlcoholFilterApplied: LiveData<Boolean> = _isAlcoholFilterApplied

    private val allCocktails = mutableListOf<Drink>()

    init {
        loadInitialCocktails()
    }

    fun setAlcoholicFilterType(type: String) {
        _isAlcoholFilterApplied.value = true
        _isFilterChanged.value = true
        _alcoholicFilterType.value = type
    }

    fun setIngredientsFilter(ingredients: List<String>) {
        _isAlcoholFilterApplied.value = false
        _isFilterChanged.value = true
        _ingredientsFilterType.value = ingredients
    }

    fun setDefaultFilterType() {
        _isAlcoholFilterApplied.value = true
        _alcoholicFilterType.value = DEFAULT_FILTER
    }

    fun getFilteredCocktailsByAlcoholType(type: String) {
        if (_isAlcoholFilterApplied.value == true && _isFilterChanged.value == true) {
            resetCocktails()
            viewModelScope.launch {
                try {
                    loadingMutable.value = true
                    val cocktails = getFilteredCocktailsByAlcoholTypeUseCase.execute(type)
                    allCocktails.addAll(cocktails.drinks)
                    getNextPageCocktails()
                } catch (e: Exception) {
                    _uiMessageChannel.emit(R.string.no_internet_connection)
                } finally {
                    loadingMutable.value = false
                }
            }
        }
    }

    fun getFilteredCocktailsByIngredients(ingredients: List<String>) {
        if (_isAlcoholFilterApplied.value == false && _isFilterChanged.value == true) {
            resetCocktails()
            viewModelScope.launch {
                try {
                    loadingMutable.value = true
                    val cocktails = getFilteredCocktailsByIngredientUseCase.execute(ingredients)
                    allCocktails.addAll(cocktails.drinks)
                    getNextPageCocktails()
                } catch (e: Exception) {
                    _uiMessageChannel.emit(R.string.no_internet_connection)
                } finally {
                    loadingMutable.value = false
                }
            }
        }
    }

    fun getNextPageCocktails() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value ?: return@launch
                val nextPage = currentState.currentPage + 1
                val nextPageCocktails = allCocktails.take(nextPage * PAGE_SIZE)
                cocktailsMutable.value = Cocktails(nextPageCocktails)

                val newState = currentState.copy(currentPage = nextPage)
                _uiState.value = newState
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_internet_connection)
            }
        }
    }

    fun setIsFilterChanged(isChanged: Boolean) {
        _isFilterChanged.value = isChanged
    }

    fun saveUiState(scrollPosition: Int, scrollOffset: Int) {
        _uiState.value = _uiState.value?.copy(
            scrollPosition = scrollPosition,
            scrollOffset = scrollOffset
        )
    }

    private fun loadInitialCocktails() {
        getFilteredCocktailsByAlcoholType(DEFAULT_FILTER)
    }

    private fun resetCocktails() {
        allCocktails.clear()
        _uiState.value = _uiState.value?.copy(currentPage = 0)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}
