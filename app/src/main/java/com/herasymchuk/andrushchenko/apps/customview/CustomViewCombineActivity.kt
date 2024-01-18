package com.herasymchuk.andrushchenko.apps.customview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.databinding.ActivityCustomViewCombineBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class CustomViewCombineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewCombineBinding
    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewCombineBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomButtons.setPositiveButtonListener {
            binding.bottomButtons.isProgressMode = true
            handler.postDelayed({
                binding.bottomButtons.isProgressMode = false
                binding.bottomButtons.setPositiveButtonText("Updated Apply")
                Toast.makeText(this, "Positive button pressed", Toast.LENGTH_SHORT).show()
            }, token, 2000)
        }
        binding.bottomButtons.setNegativeButtonListener {
            binding.bottomButtons.setNegativeButtonText("Updated Cancel")
            Toast.makeText(this, "Negative button pressed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(token)
    }
}
