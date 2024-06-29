package com.bogdash.cocktails

object Constants {

    object Main {
        const val EXTRA_CAMERA_FACING = "android.intent.extras.CAMERA_FACING"
    }

    object Splash {
        const val DELAY_TIME: Long = 2000
    }

    object Data {
        const val APP_PREFS = "app_prefs"
    }

    object HomeScreen {
        const val ALCOHOLIC = "Alcoholic"
        const val NON_ALCOHOLIC = "Non_Alcoholic"
    }

    object Filters {
        const val DEFAULT_FILTER = HomeScreen.ALCOHOLIC
    }

    object Saved {
        const val FROM_SAVED = "from_saved"
    }

    object DetailFragment {
        const val FROM_SCANNER = "from_scanner"
    }

}
