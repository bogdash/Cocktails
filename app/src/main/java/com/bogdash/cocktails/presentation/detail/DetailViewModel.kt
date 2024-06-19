package com.bogdash.cocktails.presentation.detail

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.qrScanner.QRCodeEncoder
import com.bogdash.domain.models.Drink
import com.bogdash.domain.usecases.DeleteCocktailByIdUseCase
import com.bogdash.domain.usecases.GetCocktailDetailsByIdUseCase
import com.bogdash.domain.usecases.IsCocktailSavedUseCase
import com.bogdash.domain.usecases.SaveCocktailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCocktailDetailsByIdUseCase: GetCocktailDetailsByIdUseCase,
    private val saveCocktailByIdUseCase: SaveCocktailByIdUseCase,
    private val deleteCocktailByIdUseCase: DeleteCocktailByIdUseCase,
    private val isCocktailSavedUseCase: IsCocktailSavedUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _selectedTab = MutableStateFlow(Tab.INGREDIENTS)
    val selectedTab = _selectedTab.asStateFlow()

    private val _cocktail = MutableSharedFlow<Drink>(replay = 1)
    val cocktail = _cocktail.asSharedFlow()

    private val _uiMessageChannel = MutableSharedFlow<Int>()
    val uiMessageChannel = _uiMessageChannel.asSharedFlow()

    lateinit var qr: Bitmap

    fun getCocktailDetailsByJson(json: String) {
        viewModelScope.launch {
            val cocktail = Json.decodeFromString<Drink>(json)
            commonInit(cocktail)
        }
    }

    fun getCocktailDetailsById(id: String) {
        viewModelScope.launch {
            try {
                val cocktail = getCocktailDetailsByIdUseCase.execute(id).drinks.first()
                commonInit(cocktail)
            } catch (e: Exception) {
                _uiMessageChannel.emit(R.string.no_internet_connection)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val toggledState = !_isFavorite.value
            _isFavorite.emit(toggledState)

            _cocktail.collectLatest { cocktail ->
                cocktail.isFavorite = toggledState

                try {
                    if (toggledState) {
                        saveCocktailByIdUseCase.execute(cocktail)
                    } else {
                        deleteCocktailByIdUseCase.execute(cocktail)
                    }
                } catch (e: Exception) {
                    _uiMessageChannel.emit(R.string.error_database)
                }
            }
        }
    }

    fun setSelectedTab(tabIndex: Int) {
        viewModelScope.launch {
            when (tabIndex) {
                TAB_LEFT -> _selectedTab.emit(Tab.INGREDIENTS)
                TAB_RIGHT -> _selectedTab.emit(Tab.INSTRUCTIONS)
            }
        }
    }

    private fun commonInit(drink: Drink) {
        viewModelScope.launch {
            _cocktail.emit(drink)
            _isFavorite.emit(isCocktailSavedUseCase.execute(drink.id))
            qr = getBitmapFromString(getSerializedDrink(drink))
            _isLoading.emit(false)
        }
    }

    private fun getSerializedDrink(drink: Drink): String {
        return Json.encodeToString(Drink.serializer(), drink)
    }

    private fun getBitmapFromString(s: String): Bitmap {
        return QRCodeEncoder(context).encodeAsBitmap(s, 700)
    }

    enum class Tab {
        INGREDIENTS, INSTRUCTIONS
    }

    companion object {
        const val TAB_LEFT = 0
        const val TAB_RIGHT = 1
    }

}
