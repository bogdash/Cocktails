package com.bogdash.cocktails.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val cocktailOfTheDayMutable = MutableLiveData<Drink>()
    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()
    val resultCocktailOfTheDay: LiveData<Drink> = cocktailOfTheDayMutable

    fun getCocktailOfTheDay() {
        viewModelScope.launch {
            try {
                val cocktails = getCocktailOfTheDayUseCase.execute()
                cocktailOfTheDayMutable.value = cocktails.drinks.first()
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_internet_connection)
            }
        }
    }

}
