package com.bogdash.cocktails.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.GetCocktailsByPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCocktailsByPageUseCase: GetCocktailsByPageUseCase
) : ViewModel() {

    private val cocktailsByPageMutable = MutableLiveData<Cocktails>()
    val resultCocktailsByPage: LiveData<Cocktails> = cocktailsByPageMutable

    private val loadingMutable = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loadingMutable

    private val allCocktails = mutableListOf<Drink>()
    private var currentPage = 0
    private val pageSize = 10

    init {
        viewModelScope.launch {
            try {
                loadingMutable.value = true
                val cocktails = getCocktailsByPageUseCase.execute()
                allCocktails.addAll(cocktails.drinks)
                GetNextPageCocktails()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading cocktails: $e")
            } finally {
                loadingMutable.value = false
            }
        }
    }

    fun GetNextPageCocktails() {
        viewModelScope.launch {
            try {
                val nextPageCocktails = allCocktails.take((currentPage + 1) * pageSize)
                cocktailsByPageMutable.value = Cocktails(nextPageCocktails)
                currentPage++
            } catch (e: Exception) {
                Log.e("HomeViewModel", "cocktailsByPage error: $e")
            }
        }
    }
}
