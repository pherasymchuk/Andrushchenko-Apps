package com.herasymchuk.andrushchenko.apps.recyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.MainApplication
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService
import com.herasymchuk.andrushchenko.apps.recyclerview.screens.UserDetailsFragment
import com.herasymchuk.andrushchenko.apps.recyclerview.screens.UserListFragment
import com.herasymchuk.andrushchenko.databinding.ActivityRecyclerViewBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class RecyclerViewActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityRecyclerViewBinding
    private lateinit var adapter: UsersAdapter
    private val usersService: UsersService get() = (application as MainApplication).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.root.applyInsets(top = true, left = true, right = true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, UserListFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, UserDetailsFragment.newInstance(user.id))
            .commit()
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}
