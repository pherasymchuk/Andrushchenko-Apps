package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herasymchuk.andrushchenko.MainApplication
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UserDetails
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.Result

abstract class UserDetailsViewModel(
    protected val usersService: UsersService,
) : BaseViewModel() {
    abstract val state: LiveData<State>
    abstract val actionShowToast: LiveData<Event<Int>>
    abstract val actionGoBack: LiveData<Event<Unit>>

    abstract fun loadUserDetails(id: Long)

    abstract fun deleteUser()

    data class State(
        val userDetailsResult: Result<UserDetails>,
        val deletingInProgress: Boolean,
    ) {
        fun showContent(): Boolean = userDetailsResult is Result.Success
        fun showProgress(): Boolean = userDetailsResult is Result.Pending || deletingInProgress
    }

    class Default(usersService: UsersService) : UserDetailsViewModel(usersService) {
        override val state: MutableLiveData<State> = MutableLiveData(
            State(
                userDetailsResult = Result.Empty(),
                deletingInProgress = false
            )
        )
        override val actionShowToast: MutableLiveData<Event<Int>> = MutableLiveData()
        override val actionGoBack: MutableLiveData<Event<Unit>> = MutableLiveData()

        override fun loadUserDetails(id: Long) {
            // If already loaded
            if (state.value?.userDetailsResult is Result.Success) return
            state.value = state.value?.copy(userDetailsResult = Result.Pending())
            usersService.getDetailsById(id)
                .onSuccess {
                    state.value = state.value?.copy(userDetailsResult = Result.Success(it))
                }
                .onError {
                    actionShowToast.value = Event(R.string.cant_load_user_details)
                    actionGoBack.value = Event(Unit)
                }
                .autoCancel()
        }

        override fun deleteUser() {
            val userDetailsResult: Result<UserDetails> =
                state.value?.userDetailsResult ?: Result.Error(IllegalStateException())
            if (userDetailsResult !is Result.Success) return
            state.value = state.value?.copy(deletingInProgress = true)
            usersService.deleteUser(userDetailsResult.data.user)
                .onSuccess {
                    actionShowToast.value = Event(R.string.user_has_been_deleted)
                    actionGoBack.value = Event(Unit)
                }
                .onError {
                    state.value = state.value?.copy(deletingInProgress = false)
                    actionShowToast.value = Event(R.string.cant_delete_user)
                }
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
