package com.herasymchuk.andrushchenko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.herasymchuk.andrushchenko.databinding.ActivityMainBinding
import com.herasymchuk.andrushchenko.insets.applyInsets
import com.herasymchuk.andrushchenko.repository.AppItemRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyInsets(top = true, left = true, right = true)

        setSupportActionBar(binding.toolbar)

        with(binding.content.recyclerView) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            val decoration = MaterialDividerItemDecoration(this@MainActivity, RecyclerView.VERTICAL)
            decoration.dividerThickness = 2
            addItemDecoration(decoration)
            setHasFixedSize(true)
            adapter = AppsAdapter(AppItemRepository(this@MainActivity).getAllApps())
        }
    }
}
