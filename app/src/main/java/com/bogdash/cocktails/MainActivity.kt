package com.bogdash.cocktails

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bogdash.cocktails.databinding.ActivityMainBinding
import com.bogdash.cocktails.home.HomeScreenFragment
import com.bogdash.cocktails.saved.SavedFragment
import com.bogdash.cocktails.search.SearchFragment

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
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        shakeDeviceService = ShakeDeviceService(
            this,
            CocktailOfTheDay(this).dialog
        )

        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeScreenFragment())
                R.id.saved -> replaceFragment(SavedFragment())
                R.id.search -> replaceFragment(SearchFragment())
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.home
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
        supportFragmentManager.
        beginTransaction().
        replace(R.id.fragment_container,fragment).commit()
    }
}
