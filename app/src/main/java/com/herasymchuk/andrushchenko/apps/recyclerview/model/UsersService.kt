package com.herasymchuk.andrushchenko.apps.recyclerview.model

import com.github.javafaker.Faker
import com.herasymchuk.andrushchenko.apps.recyclerview.UserNotFoundException
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.CallableTask
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.Task
import java.util.Collections

fun interface UsersListener {
    fun invoke(users: List<User>)
}

/**
 * [UsersService] is a class that provides methods for managing users.
 * It includes methods for loading users, deleting users, moving users, firing users, and getting user details.
 * The service also provides methods for adding and removing listeners to be notified of changes to the user list.
 */
class UsersService {

    private var users = mutableListOf<User>()
    private var loaded = false

    private val listeners = mutableSetOf<UsersListener>()

    init {
        loadUsers()
    }

    /**
     * Loads a list of users into the application.
     *
     * @return A task that represents the asynchronous operation.
     */
    fun loadUsers(): Task<Unit> = CallableTask {
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map {
            User(
                id = it.toLong(),
                photo = IMAGES[it % IMAGES.size],
                name = faker.name().name(),
                company = faker.company().name()
            )
        }.toMutableList()
        loaded = true
        notifyChanges()
    }

    fun deleteUser(user: User): Task<Unit> = CallableTask {
        val indexToRemove = users.indexOfFirst { it.id == user.id }
        if (indexToRemove != -1) {
            users = ArrayList(users)
            users.removeAt(indexToRemove)
        }
        notifyChanges()
    }

    fun moveUser(user: User, moveBy: Int): Task<Unit> = CallableTask {
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return@CallableTask
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex > users.lastIndex) return@CallableTask
        users = ArrayList(users)
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    }

    fun fireUser(user: User) = CallableTask {
        val index: Int = users.indexOfFirst { it.id == user.id }
        if (index == -1) return@CallableTask
        val updatedUser = user.copy(company = "")
        users = ArrayList(users)
        users[index] = updatedUser
        notifyChanges()
    }

    fun getDetailsById(id: Long): Task<UserDetails> = CallableTask {
        val user: User = users.firstOrNull { it.id == id } ?: throw UserNotFoundException()
        return@CallableTask UserDetails(
            user,
            Faker.instance().lorem().paragraphs(3).joinToString(separator = "\n\n")
        )
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        if (loaded) {
            listener.invoke(users)
        }
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        if (!loaded) return
        listeners.forEach { it.invoke(users) }
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1704875940244-299752f81346?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1600961460202-c8b482d59388?q=80&w=1471&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://plus.unsplash.com/premium_photo-1661890071978-6c80f92c7fdf?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1549854233-ca0baec6fa74?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1627848219665-a8f24581387d?q=80&w=1364&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1702878650631-7033c64fcc01?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1704174802414-2dbc7d476d30?q=80&w=1376&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1704394187489-76c2a7683e0d?q=80&w=1502&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1704265586219-9cdd2857a275?q=80&w=1335&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1704265586326-6b8d7569e62c?q=80&w=1335&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        )
    }
}
