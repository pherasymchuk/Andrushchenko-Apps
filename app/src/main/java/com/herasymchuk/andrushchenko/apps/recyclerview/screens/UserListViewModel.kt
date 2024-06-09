package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herasymchuk.andrushchenko.MainApplication
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersListener
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService

abstract class UserListViewModel(
    protected val usersService: UsersService,
) : ViewModel() {
    abstract val users: LiveData<List<User>>

    abstract fun loadUsers()

    abstract fun moveUser(user: User, moveBy: Int)

    abstract fun deleteUser(user: User)

    abstract fun fireUser(user: User)

    class Default(usersService: UsersService) : UserListViewModel(usersService) {
        override val users: MutableLiveData<List<User>> = MutableLiveData()

        private val listener = UsersListener {
            users.value = it
        }

        init {
            loadUsers()
        }

        override fun loadUsers() {
            usersService.addListener(listener)
        }

        override fun onCleared() {
            usersService.removeListener(listener)
        }

        override fun moveUser(user: User, moveBy: Int) {
            usersService.moveUser(user, moveBy)
        }

        override fun deleteUser(user: User) {
            usersService.deleteUser(user)
        }

        override fun fireUser(user: User) {
            usersService.fireUser(user)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            addInitializer(Default::class) {
                val application: MainApplication =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication
                Default(application.usersService)
            }
        }
    }
}
