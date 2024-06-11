package com.bogdash.cocktails.presentation.cocktailoftheday

import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.main.MainActivity
import com.bogdash.domain.models.Drink
import com.bumptech.glide.Glide

/**
 * Cocktail of the day dialog wrapper.
 *
 * @param activity activity that shows the dialog
 */
class CocktailOfTheDay(
    private val activity: Activity,
    private val drink: Drink
) {

    /**
     * Shows a dialog with a cocktail of the day.
     */
    val dialog: () -> Unit = {
        val builder = AlertDialog.Builder(activity, R.style.ShakeAlertDialog).create()
        val view = activity.layoutInflater.inflate(R.layout.shake_dialog, null)
        val buttonCancel = view.findViewById<TextView>(R.id.tv_cancel)
        val buttonReadMore = view.findViewById<TextView>(R.id.tv_read_more)
        val cocktailName = view.findViewById<TextView>(R.id.tv_name_cocktail)
        val cocktailImage = view.findViewById<ImageView>(R.id.img_cocktail)
        val numberOfIngredientsTextView = view.findViewById<TextView>(R.id.tv_numbers_of_ingredients)
        val alcoholicCocktail = view.findViewById<TextView>(R.id.tv_alcohol)

        val numberOfIngredients = drink.ingredients?.size
        val numberOfIngredientsText = numberOfIngredients?.let {
            activity.resources.getQuantityString(
                R.plurals.number_of_ingredients,
                it,
                numberOfIngredients
            )
        }

        cocktailName.text = drink.name
        numberOfIngredientsTextView.text = numberOfIngredientsText
        alcoholicCocktail.text = drink.alcoholic
        Glide.with(activity).load(drink.thumb).into(cocktailImage)

        with(builder) {
            setView(view)
            buttonCancel.setOnClickListener {
                dismiss()
                (activity as? MainActivity)?.onDialogDismiss()
            }
            buttonReadMore.setOnClickListener {
                // TODO: go to detailed screen
                dismiss()
                (activity as? MainActivity)?.onDialogDismiss()
            }
            setCanceledOnTouchOutside(true)
            setOnDismissListener {
                (activity as? MainActivity)?.onDialogDismiss()
            }
            show()
        }
    }

}
