package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herasymchuk.andrushchenko.MainApplication
import com.herasymchuk.andrushchenko.apps.recyclerview.UserNotFoundException
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UserDetails
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService

abstract class UserDetailsViewModel(
    protected val usersService: UsersService,
) : ViewModel() {
    abstract val userDetails: LiveData<UserDetails>

    abstract fun loadUserDetails(id: Long)

    abstract fun deleteUser()

    class Default(usersService: UsersService) : UserDetailsViewModel(usersService) {
        override val userDetails: MutableLiveData<UserDetails> = MutableLiveData()

        override fun loadUserDetails(id: Long) {
            // If already loaded
            if (userDetails.value != null) return
            try {
                userDetails.value = usersService.getDetailsById(id)
            } catch (e: UserNotFoundException) {
                e.printStackTrace()
            }
        }

        override fun deleteUser() {
            val userDetails = userDetails.value ?: return
            usersService.deleteUser(userDetails.user)
        }

    }

    companion object {
        val Factory = viewModelFactory {
            addInitializer(UserDetailsViewModel::class) {
                val application: MainApplication =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication
                Default(application.usersService)
            }
        }
    }
}
