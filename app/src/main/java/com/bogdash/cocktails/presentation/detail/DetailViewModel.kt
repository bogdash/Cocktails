package com.bogdash.cocktails.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.DeleteCocktailByIdUseCase
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.GetSavedCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.IsCocktailSavedUseCase
import com.bogdash.domain.usecases.SaveCocktailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCocktailDetailsByIdUseCase: GetCocktailDetailsByIdUseCase,
    private val getSavedCocktailDetailsByIdUseCase: GetSavedCocktailDetailsByIdUseCase,
    private val saveCocktailByIdUseCase: SaveCocktailByIdUseCase,
    private val deleteCocktailByIdUseCase: DeleteCocktailByIdUseCase,
    private val isCocktailSavedUseCase: IsCocktailSavedUseCase
) : ViewModel() {

    private val loadingStateMutable = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = loadingStateMutable

    private val detailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = detailsMutable

    private val favoriteStateMutable = MutableLiveData(false)
    val favoriteState: LiveData<Boolean> = favoriteStateMutable

    private val selectedTabMutable = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> = selectedTabMutable

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    private lateinit var currentDrink: Drink

    fun getCocktailDetailsById(id: String) {
        viewModelScope.launch {
            loadingStateMutable.value = true
            try {
                val details = getCocktailDetailsByIdUseCase.execute(id)
                detailsMutable.value = details
                currentDrink = details.drinks.first()
                favoriteStateMutable.value = isCocktailSavedUseCase.execute(currentDrink.id)
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_internet_connection)
            } finally {
                loadingStateMutable.value = false
            }
        }
    }

    fun getSavedCocktailDetailsById(id: String) {
        viewModelScope.launch {
            loadingStateMutable.value = true
            try {
                val details = getSavedCocktailDetailsByIdUseCase.execute(id)
                detailsMutable.value = details
                currentDrink = details.drinks.first()
                favoriteStateMutable.value = isCocktailSavedUseCase.execute(currentDrink.id)
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_select_saved)
            } finally {
                loadingStateMutable.value = false
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
                    favoriteStateMutable.value = drink.isFavorite
                } catch (e: Exception) {
                    _uiMessageChannel.emit(R.string.error_database)
                }
            }
        }
    }

    fun setSelectedTab(tabIndex: Int) {
        selectedTabMutable.value = tabIndex
    }

}
