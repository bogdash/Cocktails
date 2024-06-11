package com.bogdash.cocktails.presentation.cocktailoftheday

import android.app.Activity
import android.app.Dialog
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.detail.DetailFragment
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

        val numberOfIngredients = drink.ingredients.size
        val numberOfIngredientsText = activity.resources.getQuantityString(
            R.plurals.number_of_ingredients,
            numberOfIngredients,
            numberOfIngredients
        )

        cocktailName.text = drink.name
        numberOfIngredientsTextView.text = numberOfIngredientsText
        alcoholicCocktail.text = drink.alcoholic
        Glide.with(activity).load(drink.thumb).into(cocktailImage)

        with(builder) {
            setView(view)
            setupCancelClickListener(buttonCancel, this)
            setupReadMoreClickListener(buttonReadMore, this)
            setCanceledOnTouchOutside(true)
            setOnDismissListener {
                (activity as? MainActivity)?.onDialogDismiss()
            }
            show()
        }
    }

    private fun setupCancelClickListener(button: TextView, dialog: AlertDialog) {
        button.setOnClickListener {
            dialog.dismiss()
            (activity as? MainActivity)?.onDialogDismiss()
        }
    }

    private fun setupReadMoreClickListener(button: TextView, dialog: AlertDialog) {
        button.setOnClickListener {
            dialog.dismiss()
            (activity as? FragmentActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, DetailFragment.newInstance(drink.id))
                addToBackStack(null)
                commit()
            }
            (activity as? MainActivity)?.onDialogDismiss()
        }
    }

}
