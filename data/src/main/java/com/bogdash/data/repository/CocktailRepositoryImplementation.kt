package com.bogdash.data.repository

import com.bogdash.data.storage.database.dao.DrinkDao
import com.bogdash.data.storage.database.dao.IngredientDao
import com.bogdash.data.storage.database.entities.DrinkEntity
import com.bogdash.data.storage.database.entities.IngredientEntity
import com.bogdash.data.storage.network.ConstantsForNetwork.DATE_FORMAT_PATTERN
import com.bogdash.data.storage.network.retrofit.CocktailsApiService
import com.bogdash.data.storage.preferences.CocktailPreferences
import com.bogdash.domain.models.Cocktails
import com.bogdash.domain.models.Drink
import com.bogdash.domain.models.Ingredient
import com.bogdash.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            val drinkEntity = DrinkEntity(
                id = drink.id,
                name = drink.name,
                tags = drink.tags,
                category = drink.category,
                iba = drink.iba,
                alcoholic = drink.alcoholic,
                glass = drink.glass,
                instructions = drink.instructions,
                thumb = drink.thumb,
                creativeCommonsConfirmed = drink.creativeCommonsConfirmed,
                dateModified = drink.dateModified,
                isFavorite = false
            )
            drinkDao.insertDrink(drinkEntity)

            val ingredientEntities = drink.ingredients.map { ingredient ->
                IngredientEntity(
                    name = ingredient.name,
                    measure = ingredient.measure ?: "",
                    drinkId = drink.id
                )
            }
            ingredientDao.insertIngredients(ingredientEntities)
        }
    }

    override suspend fun getCocktailById(id: String): Drink {
        return withContext(Dispatchers.IO) {
            val drinkEntity = drinkDao.getDrinkById(id)
            val ingredientEntities = ingredientDao.getIngredientsByDrinkId(id)

            if (drinkEntity != null && ingredientEntities.isNotEmpty()) {
                Drink(
                    id = drinkEntity.id,
                    name = drinkEntity.name?: "",
                    tags = drinkEntity.tags,
                    category = drinkEntity.category?: "",
                    iba = drinkEntity.iba,
                    alcoholic = drinkEntity.alcoholic?: "",
                    glass = drinkEntity.glass?: "",
                    instructions = drinkEntity.instructions?: "",
                    thumb = drinkEntity.thumb?: "",
                    ingredients = ingredientEntities.map { Ingredient(it.name, it.measure) },
                    creativeCommonsConfirmed = drinkEntity.creativeCommonsConfirmed,
                    dateModified = drinkEntity.dateModified?: ""
                )
            } else {
                throw Exception("Drink not found")
            }
        }
    }

    override suspend fun deleteCocktailById(id: String) {
        withContext(Dispatchers.IO) {
            drinkDao.deleteDrinkById(id)
            ingredientDao.deleteIngredientsByDrinkId(id)
        }
    }

}
