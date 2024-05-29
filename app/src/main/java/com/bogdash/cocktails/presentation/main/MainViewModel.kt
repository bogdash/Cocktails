package com.bogdash.cocktails.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.GetCocktailOfTheDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCocktailOfTheDayUseCase: GetCocktailOfTheDayUseCase
) : ViewModel() {

    private var resultLiveDataMutable = MutableLiveData<String>()
    val resultLiveData: LiveData<String> = resultLiveDataMutable

    private val cocktailOfTheDayMutable = MutableLiveData<Drink>()
    val resultCocktailOfTheDay: LiveData<Drink> = cocktailOfTheDayMutable

    fun getCocktailOfTheDay() {
        viewModelScope.launch {
            try {
                val cocktails = getCocktailOfTheDayUseCase.execute()
                cocktailOfTheDayMutable.value = cocktails.drinks.first()
            } catch (e: Exception) {
                Log.d("MyLog", "cocktailOfTheDay error: $e")
            }
        }
    }

}
