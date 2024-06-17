package com.bogdash.cocktails.presentation.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.domain.models.CocktailsWithCategory
import com.bogdash.domain.usecases.GetSavedCocktailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getSavedCocktailsUseCase: GetSavedCocktailsUseCase
) : ViewModel(){

    private val cocktailsWithCategoriesMutable = MutableLiveData<List<CocktailsWithCategory>>()
    val resultCocktailsWithCategories: LiveData<List<CocktailsWithCategory>> = cocktailsWithCategoriesMutable

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    private val loadingStateMutable = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = loadingStateMutable
    fun getCocktailsWithCategories(){
        loadingStateMutable.value = true
        viewModelScope.launch {
            try{
                val cocktailsWithCategory = getSavedCocktailsUseCase.execute()
                cocktailsWithCategoriesMutable.value = cocktailsWithCategory
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.error_select_saved)
            } finally {
                loadingStateMutable.value = false
            }
        }
    }
}
