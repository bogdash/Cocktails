package com.bogdash.cocktails.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.DeleteCocktailByIdUseCase
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.SaveCocktailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCocktailDetailsByIdUseCase: GetCocktailDetailsByIdUseCase,
    private val saveCocktailByIdUseCase: SaveCocktailByIdUseCase,
    private val deleteCocktailByIdUseCase: DeleteCocktailByIdUseCase
) : ViewModel() {
    private val detailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = detailsMutable

    private val favoriteStateMutable = MutableLiveData<Boolean>()
    val favoriteState: LiveData<Boolean> = favoriteStateMutable

    private val selectedTabMutable = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> = selectedTabMutable

    private var currentDrink: Drink? = null

    fun getCocktailDetailsById(id: String) {
        viewModelScope.launch {
            try {
                val details = getCocktailDetailsByIdUseCase.execute(id)
                detailsMutable.value = details
                currentDrink = details.drinks.firstOrNull()
                favoriteStateMutable.value = currentDrink?.isFavorite
            } catch (e: Exception) {
                Log.d("MyLog", "DetailViewModel $e")
            }
        }
    }

    fun toggleFavorite() {
        val currentState = favoriteStateMutable.value ?: false
        favoriteStateMutable.value = !currentState
        currentDrink?.let {  drink ->
            drink.isFavorite = !currentState
            viewModelScope.launch {
                try {
                    if (currentState) {
                        deleteCocktailByIdUseCase.execute(drink)
                    } else {
                        saveCocktailByIdUseCase.execute(drink)
                    }
                } catch (e: Exception) {
                    Log.d("MyLog", "DetailViewModel toggleFavorite $e")
                }
            }
        }
    }

    fun setSelectedTab(tabIndex: Int) {
        selectedTabMutable.value = tabIndex
    }
}