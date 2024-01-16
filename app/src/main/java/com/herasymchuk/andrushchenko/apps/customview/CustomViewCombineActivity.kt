package com.herasymchuk.andrushchenko.apps.customview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.databinding.ActivityCustomViewCombineBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class CustomViewCombineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewCombineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewCombineBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
    }
}
