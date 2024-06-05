package com.bogdash.cocktails.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCocktailDetailsByIdUseCase: GetCocktailDetailsByIdUseCase
) : ViewModel() {
    private val detailsMutable = MutableLiveData<Cocktails>()
    val resultCocktails: LiveData<Cocktails> = detailsMutable

    fun getCocktailDetailsById(id: String) {
        viewModelScope.launch {
            try {
                val details = getCocktailDetailsByIdUseCase.execute(id)
                detailsMutable.value = details
            } catch (e: Exception) {
                Log.d("MyLog", "DetailViewModel $e")
            }
        }
    }
}