package com.bogdash.cocktails

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

/**
 * Cocktail of the day dialog wrapper.
 *
 * @param activity activity that shows the dialog
 */
class CocktailOfTheDay(
    private val activity: Activity
) {

    /**
     * Shows a dialog with a cocktail of the day.
     */
    val dialog: () -> Unit = {
        val builder = AlertDialog.Builder(activity, R.style.ShakeAlertDialog).create()
        val view = activity.layoutInflater.inflate(R.layout.shake_dialog, null)
        val buttonCancel = view.findViewById<TextView>(R.id.tv_cancel)
        val buttonReadMore = view.findViewById<TextView>(R.id.tv_read_more)

        with(builder) {
            setView(view)
            buttonCancel.setOnClickListener {
                dismiss()
            }
            buttonReadMore.setOnClickListener {
                // TODO: go to detailed screen
            }
            setCanceledOnTouchOutside(true)
            show()
        }
    }

}
