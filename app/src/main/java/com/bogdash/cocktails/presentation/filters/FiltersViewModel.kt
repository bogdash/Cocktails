package com.bogdash.cocktails.presentation.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.usecases.GetFilteredCocktailsByAlcoholTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getFilteredCocktailsByAlcoholTypeUseCase: GetFilteredCocktailsByAlcoholTypeUseCase
) : ViewModel() {

    private val _filteredCocktails = MutableLiveData<Cocktails>()
    val filteredCocktails: LiveData<Cocktails> = _filteredCocktails

    private val _filterType = MutableLiveData<String>()
    val filterType: LiveData<String> = _filterType

    private val _uiMessageChannel: MutableSharedFlow<Int> = MutableSharedFlow()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    fun setFilterType(type: String) {
        _filterType.value = type
    }

    fun applyFilters() {
        _filterType.value?.let { type ->
            viewModelScope.launch {
                try {
                    val cocktails = getFilteredCocktailsByAlcoholTypeUseCase.execute(type)
                    _filteredCocktails.value = cocktails
                } catch (e: Exception) {
                    _uiMessageChannel.emit(R.string.error_loading_cocktails)
                }
            }
        }
    }
}