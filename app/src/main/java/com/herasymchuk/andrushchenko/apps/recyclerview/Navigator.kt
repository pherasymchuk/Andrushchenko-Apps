package com.herasymchuk.andrushchenko.apps.recyclerview

import androidx.annotation.StringRes
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User

interface Navigator {
    fun showDetails(user: User)
    fun goBack()
    fun toast(@StringRes messageRes: Int)
}
