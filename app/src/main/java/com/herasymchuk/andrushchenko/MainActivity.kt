package com.herasymchuk.andrushchenko

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.databinding.ActivityMainBinding
import com.herasymchuk.andrushchenko.insets.applyInsets
import com.herasymchuk.andrushchenko.repository.AppItemRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.content.recyclerView.adapter = AppsAdapter(AppItemRepository(this).getAllApps())
        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)

        setSupportActionBar(binding.toolbar)
    }
}
