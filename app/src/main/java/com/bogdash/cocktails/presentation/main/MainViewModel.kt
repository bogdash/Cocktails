package com.bogdash.cocktails.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.GetCocktailOfTheDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCocktailOfTheDayUseCase: GetCocktailOfTheDayUseCase
) : ViewModel() {

    private val _cocktailOfTheDay = MutableSharedFlow<Drink>()
    val cocktailOfTheDay = _cocktailOfTheDay.asSharedFlow()

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    fun getCocktailOfTheDay() {
        viewModelScope.launch {
            try {
                val cocktails = getCocktailOfTheDayUseCase.execute()
                val drink = cocktails.drinks.first()
                _cocktailOfTheDay.emit(drink)
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_internet_connection)
            }
        }
    }

}
