package com.herasymchuk.andrushchenko.model

data class AppItem(
    val name: String,
    val onButtonClick: () -> Unit,
)
