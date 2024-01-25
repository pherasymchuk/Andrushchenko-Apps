package com.herasymchuk.andrushchenko

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.herasymchuk.andrushchenko.apps.recyclerview.model.UsersService

class MainApplication : Application() {
    val usersService: UsersService = UsersService()
    override fun onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this)
        super.onCreate()
    }
}
