package com.bogdash.cocktails.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.usecases.SearchCocktailsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCocktailsByNameUseCase: SearchCocktailsByNameUseCase
) : ViewModel(){
    private val cocktailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = cocktailsMutable

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    private val loadingStateMutable = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = loadingStateMutable

    fun searchCocktailsByName(name: String) {
        loadingStateMutable.value = true
        viewModelScope.launch {
            try {
                val cocktails = searchCocktailsByNameUseCase.execute(name)
                cocktailsMutable.value = cocktails
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_queries_search)
            } finally {
                loadingStateMutable.value = false
            }
        }
    }
}
