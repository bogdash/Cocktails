package com.bogdash.cocktails.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bogdash.cocktails.presentation.cocktailoftheday.CocktailOfTheDay
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.cocktailoftheday.ShakeDeviceService
import com.bogdash.cocktails.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var shakeDeviceService: ShakeDeviceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        shakeDeviceService = ShakeDeviceService(
            this,
            CocktailOfTheDay(this).dialog
        )
    }

    override fun onResume() {
        super.onResume()
        shakeDeviceService.subscribe()
    }

    override fun onPause() {
        super.onPause()
        shakeDeviceService.unsubscribe()
    }

}
