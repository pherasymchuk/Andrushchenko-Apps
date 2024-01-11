package com.herasymchuk.andrushchenko.model

import androidx.navigation.NavController

data class AppItem(
    val name: String,
    val onButtonClick: (NavController) -> Unit,
)
