package com.bogdash.cocktails

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CocktailOfTheDay(
    private val activity: Activity
) {

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
