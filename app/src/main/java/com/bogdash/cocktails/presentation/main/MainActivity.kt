package com.bogdash.cocktails.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bogdash.cocktails.Constants.DetailFragment.FROM_SCANNER
import com.bogdash.cocktails.presentation.cocktailoftheday.CocktailOfTheDay
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.cocktailoftheday.ShakeDeviceService
import com.bogdash.cocktails.databinding.ActivityMainBinding
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.home.HomeFragment
import com.bogdash.cocktails.presentation.qrScanner.ActivityCapture
import com.bogdash.cocktails.presentation.saved.SavedFragment
import com.bogdash.cocktails.presentation.search.SearchFragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.bogdash.cocktails.Constants.Main.EXTRA_CAMERA_FACING

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var barLauncher: ActivityResultLauncher<ScanOptions>
    private var shakeDeviceService: ShakeDeviceService? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var isDialogOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        observeViewModel()

        shakeDeviceService = ShakeDeviceService(
            this
        ) {
            if (!isDialogOpen) {
                mainViewModel.getCocktailOfTheDay()
            }
        }

        initBarLauncher()
        setupBottomNavigationBar()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun initBarLauncher() {
        barLauncher = registerForActivityResult(
            ScanContract()
        ) { result ->
            if (result.contents != null) {
                goToDetailFragment(result)
            } else {
                binding.bottomNavigationView.selectedItemId = R.id.home
            }
        }

        setupBottomNavigationBar()
    }

    private fun goToDetailFragment(result: ScanIntentResult) {
        val fragment = DetailFragment(
            DetailFragment.Input.Json(result.contents)
        ).apply {
            arguments = Bundle().apply {
                putBoolean(FROM_SCANNER, true)
            }
        }
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, fragment
        ).commit()
    }


    private fun setupBottomNavigationBar() {
        with(binding) {
            bottomNavigationView.itemIconTintList = null
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.saved -> replaceFragment(SavedFragment())
                    R.id.search -> replaceFragment(SearchFragment())
                    R.id.qr -> initQrScanner()
                }
                true
            }
            bottomNavigationView.selectedItemId = R.id.home
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDeviceService?.subscribe()
    }

    override fun onPause() {
        super.onPause()
        shakeDeviceService?.unsubscribe()
    }

    private fun observeViewModel() {
        mainViewModel.resultCocktailOfTheDay.observe(this) { drink ->
            if (drink != null) {
                val dialog = CocktailOfTheDay(this, drink)
                isDialogOpen = true
                dialog.dialog()
            }
        }
        lifecycleScope.launch {
            mainViewModel.uiMessageChannel.collect {
                Toast.makeText(this@MainActivity, getString(it), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initQrScanner() {
        val options = ScanOptions()
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(ActivityCapture::class.java)
        options.addExtra(EXTRA_CAMERA_FACING, 0)

        barLauncher.launch(options)
    }

    fun onDialogDismiss() {
        isDialogOpen = false
    }

}
