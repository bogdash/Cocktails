package com.bogdash.cocktails.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.usecases.SearchCocktailsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCocktailsByNameUseCase: SearchCocktailsByNameUseCase
) : ViewModel(){
    private val cocktailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = cocktailsMutable

    fun searchCocktailsByName(name: String) {
        viewModelScope.launch {
            try {
                val cocktails = searchCocktailsByNameUseCase.execute(name)
                cocktailsMutable.value = cocktails
            } catch (e: Exception) {
                Log.d("MyLog", "searchByName error: $e")
            }
        }
    }
}
