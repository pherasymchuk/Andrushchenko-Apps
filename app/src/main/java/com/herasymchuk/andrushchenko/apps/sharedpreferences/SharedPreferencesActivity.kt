package com.herasymchuk.andrushchenko.apps.sharedpreferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.herasymchuk.andrushchenko.databinding.ActivitySharedPreferencesBinding

const val APP_PREFERENCES = "APP_PREFERENCES"
const val PREF_TEXT_VALUE_KEY = "PREF_TEXT_VALUE_KEY"

class SharedPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharedPreferencesBinding
    private lateinit var preferences: SharedPreferences

    private val preferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            if (key == PREF_TEXT_VALUE_KEY) {
                binding.currentValue.text = preferences.getString(key, "")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onSupportNavigateUp() }

        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val currentValue = preferences.getString(PREF_TEXT_VALUE_KEY, "")
        if (preferences.contains(PREF_TEXT_VALUE_KEY)) {
            binding.valueEditText.setText(currentValue)
            binding.currentValue.text = currentValue
        } else {
            binding.valueEditText.setHint("Preferences was empty")
        }

        binding.saveButton.setOnClickListener {
            val inputText = (binding.valueEditText.text.toString())
            preferences.edit().putString(PREF_TEXT_VALUE_KEY, inputText).apply()
        }

        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences.unregisterOnSharedPreferenceChangeListener(preferencesListener)
    }
}
