package com.herasymchuk.andrushchenko.apps.recyclerview.screens

class Event<T>(
    private val value: T,
) {
    private var handled: Boolean = false

    fun getValue(): T? {
        if (handled) return null
        return value.also { handled = true }
    }
}
