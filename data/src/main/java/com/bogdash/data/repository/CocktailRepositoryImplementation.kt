package com.bogdash.data.repository

import com.bogdash.data.mappers.DrinkMapper
import com.bogdash.data.mappers.IngredientMapper
import com.bogdash.data.storage.database.dao.DrinkDao
import com.bogdash.data.storage.database.dao.IngredientDao
import com.bogdash.data.storage.network.ConstantsForNetwork.DATE_FORMAT_PATTERN
import com.bogdash.data.storage.network.ConstantsForNetwork.INGREDIENT_PARAMETER
import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.data.storage.preferences.CocktailPreferences
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DRINK_NOT_FOUND = "Drink not found"

class CocktailRepositoryImplementation(
    private val cocktailApiService: CocktailsApiService,
    private val cocktailPreferences: CocktailPreferences,
    private val drinkDao: DrinkDao,
    private val ingredientDao: IngredientDao
) : CocktailRepository {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    override suspend fun getCocktailOfTheDay(): Cocktails {
        return withContext(Dispatchers.IO) {
            val today = dateFormat.format(Date())

            val lastUpdateDate = cocktailPreferences.getLastUpdateDate()
            val savedDrink = cocktailPreferences.getCocktail()

            if (lastUpdateDate == today && savedDrink != null) {
                Cocktails(listOf(savedDrink))
            } else {
                val dataCocktails = cocktailApiService.getCocktailOfTheDay()
                val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })

                val drink = dataCocktails.drinks.firstOrNull()
                if (drink != null) {
                    cocktailPreferences.saveCocktail(drink.toDomain())
                    cocktailPreferences.saveLastUpdateDate(today)
                }

                domainCocktails
            }
        }
    }

    override suspend fun getCocktailDetailsById(id: String): Cocktails {
        return withContext(Dispatchers.IO) {
            val dataCocktails = cocktailApiService.getCocktailDetailsById(id)
            val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })

            domainCocktails
        }
    }

    override suspend fun saveCocktail(drink: Drink) {
        withContext(Dispatchers.IO) {
            val drinkEntity = DrinkMapper.toEntity(drink)
            drinkDao.insertDrink(drinkEntity)

            val ingredientEntities =
                drink.ingredients?.map { IngredientMapper.toEntity(it, drink.id) }
            ingredientEntities?.let { ingredientDao.insertIngredients(it) }
        }
    }

    override suspend fun getCocktailById(id: String): Drink {
        return withContext(Dispatchers.IO) {
            val drinkEntity = drinkDao.getDrinkById(id)
            val ingredientEntities = ingredientDao.getIngredientsByDrinkId(id)

            if (drinkEntity != null && ingredientEntities.isNotEmpty()) {
                DrinkMapper.fromEntity(drinkEntity, ingredientEntities)
            } else {
                throw Exception(DRINK_NOT_FOUND)
            }
        }
    }

    override suspend fun isCocktailByIdSaved(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val drinkEntity = drinkDao.getDrinkById(id)
            val ingredientEntities = ingredientDao.getIngredientsByDrinkId(id)

            drinkEntity != null && ingredientEntities.isNotEmpty()
        }
    }

    override suspend fun deleteCocktailById(id: String) {
        withContext(Dispatchers.IO) {
            drinkDao.deleteDrinkById(id)
            ingredientDao.deleteIngredientsByDrinkId(id)
        }
    }

    override suspend fun getFilteredCocktailsByAlcoholType(type: String): Cocktails {
        return withContext(Dispatchers.IO) {
            val dataCocktails = cocktailApiService.getFilteredCocktailsByAlcoholType(type)
            val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })
            domainCocktails
        }
    }

    override suspend fun getFilteredCocktailsByIngredient(ingredients: List<String>): Cocktails {
        return withContext(Dispatchers.IO) {
            val dataCocktails = cocktailApiService.getFilteredCocktailsByIngredient(ingredients)
            val domainCocktails = Cocktails(dataCocktails.drinks.map { it.toDomain() })
            domainCocktails
        }
    }
}
