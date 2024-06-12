package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import androidx.lifecycle.ViewModel
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.Task

abstract class BaseViewModel : ViewModel() {
    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel() {
        tasks.add(this)
    }
}
