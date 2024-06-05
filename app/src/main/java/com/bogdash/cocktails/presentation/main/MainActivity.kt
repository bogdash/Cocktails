package com.bogdash.cocktails.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bogdash.cocktails.presentation.cocktailoftheday.CocktailOfTheDay
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.cocktailoftheday.ShakeDeviceService
import com.bogdash.cocktails.databinding.ActivityMainBinding
import com.bogdash.cocktails.presentation.home.HomeFragment
import com.bogdash.cocktails.presentation.saved.SavedFragment
import com.bogdash.cocktails.presentation.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        shakeDeviceService = ShakeDeviceService(
            this,
            CocktailOfTheDay(this).dialog
        )

        setupBottomNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        shakeDeviceService.subscribe()
    }

    override fun onPause() {
        super.onPause()
        shakeDeviceService.unsubscribe()
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }

    private fun setupBottomNavigationBar(){
        with(binding){
            bottomNavigationView.itemIconTintList = null
            bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.saved -> replaceFragment(SavedFragment())
                    R.id.search -> replaceFragment(SearchFragment())
                }
                true
            }
            bottomNavigationView.selectedItemId = R.id.home
        }
    }
}
