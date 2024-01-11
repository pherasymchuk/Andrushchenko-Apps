package com.herasymchuk.andrushchenko

import android.app.Application
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService

class App : Application() {
    val usersService: UsersService = UsersService()
}
