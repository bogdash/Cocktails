package com.bogdash.cocktails

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bogdash.cocktails.databinding.ActivityMainBinding
import com.bogdash.cocktails.databinding.FragmentDetailedBinding
import com.bogdash.cocktails.detail.Detailed

class MainActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding
    private lateinit var binding: FragmentDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = FragmentDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}