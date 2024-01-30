package com.herasymchuk.andrushchenko.apps.customview.fromscratch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.databinding.ActivityCustomViewFromScratchBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class CustomViewFromScratchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewFromScratchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCustomViewFromScratchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
