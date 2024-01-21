package com.herasymchuk.andrushchenko.apps.customview.compoundlayout

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.databinding.ActivityCompoundCustomViewBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class CompoundCustomViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompoundCustomViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompoundCustomViewBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomButtons.setPositiveButtonListener {
            binding.bottomButtons.setPositiveButtonText("Updated Apply")
        }

        binding.bottomButtons.setNegativeButtonListener {
            binding.bottomButtons.setNegativeButtonText("Updated Cancel")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
