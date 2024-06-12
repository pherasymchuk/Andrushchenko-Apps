package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herasymchuk.andrushchenko.MainApplication
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.recyclerview.UserActionListener
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersListener
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.Result

abstract class UserListViewModel(
    protected val usersService: UsersService,
) : BaseViewModel(), UserActionListener {
    abstract val users: LiveData<Result<List<UserListItem>>>
    abstract val actionShowDetails: LiveData<Event<User>>
    abstract val actionShowToast: LiveData<Event<Int>>

    abstract fun loadUsers()

    data class UserListItem(
        val user: User,
        val inProgress: Boolean,
    )

    class Default(usersService: UsersService) :
        UserListViewModel(usersService) {
        override val users: MutableLiveData<Result<List<UserListItem>>> = MutableLiveData()
        override val actionShowDetails: MutableLiveData<Event<User>> = MutableLiveData()
        override val actionShowToast: MutableLiveData<Event<Int>> = MutableLiveData()
        private val userIdsInProgress = mutableSetOf<Long>()
        private var usersResult: Result<List<User>> = Result.Empty()
            set(value) {
                field = value
                notifyUpdates()
            }

        private val listener = UsersListener { newUsers ->
            usersResult = if (newUsers.isEmpty()) Result.Empty()
            else Result.Success(newUsers)
        }

        init {
            usersService.addListener(listener)
            loadUsers()
        }

        override fun loadUsers() {
            usersResult = Result.Pending()
            usersService.loadUsers()
                .onError {
                    usersResult = Result.Error(it)
                }
                .autoCancel()
        }

        override fun onCleared() {
            usersService.removeListener(listener)
        }

        private fun addProgressTo(user: User) {
            userIdsInProgress.add(user.id)
            notifyUpdates()

        }

        private fun removeProgressFrom(user: User) {
            userIdsInProgress.remove(user.id)
            notifyUpdates()
        }

        private fun isInProgress(user: User): Boolean {
            return userIdsInProgress.contains(user.id)
        }

        private fun notifyUpdates() {
            users.postValue(usersResult.map { users ->
                users.map { user -> UserListItem(user, isInProgress(user)) }
            })
        }

        override fun onUserMove(user: User, moveBy: Int) {
            if (isInProgress(user)) return
            addProgressTo(user)
            usersService.moveUser(user, moveBy)
                .onError {
                    removeProgressFrom(user)
                    actionShowToast.value = Event(R.string.cant_move_user)
                }
                .onSuccess { removeProgressFrom(user) }
                .autoCancel()
        }

        override fun onUserDelete(user: User) {
            if (isInProgress(user)) return
            addProgressTo(user)
            usersService.deleteUser(user)
                .onError {
                    removeProgressFrom(user)
                    actionShowToast.value = Event(R.string.cant_delete_user)
                }
                .onSuccess { removeProgressFrom(user) }
                .autoCancel()
        }

        override fun onUserDetails(user: User) {
            actionShowDetails.value = Event(user)
        }

        override fun onUserFire(user: User) {
            if (isInProgress(user)) return
            addProgressTo(user)
            usersService.fireUser(user)
                .onError { removeProgressFrom(user) }
                .onSuccess { removeProgressFrom(user) }
                .autoCancel()
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
