package com.bogdash.cocktails.presentation.home

import android.util.Log
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

    fun GetCocktailsByPage() {
        viewModelScope.launch {
            try {
                val cocktails = getCocktailsByPageUseCase.execute()
                cocktailsByPageMutable.value = cocktails
            } catch (e: Exception) {
                Log.d("MyLog", "cocktailsByPage error: $e")
            }
        }
    }
}
