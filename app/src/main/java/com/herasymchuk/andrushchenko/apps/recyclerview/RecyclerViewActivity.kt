package com.herasymchuk.andrushchenko.apps.recyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.herasymchuk.andrushchenko.App
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersListener
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService
import com.herasymchuk.andrushchenko.databinding.ActivityRecyclerViewBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewBinding
    private lateinit var adapter: UsersAdapter
    private val usersService: UsersService get() = (application as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(getColor(R.color.dark_gray_semi_transparent)))
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@RecyclerViewActivity, "User: ${user.name}", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        binding.recyclerView.adapter = adapter
        usersService.addListener(usersListener)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}
