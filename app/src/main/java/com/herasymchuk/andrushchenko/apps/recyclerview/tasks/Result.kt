package com.herasymchuk.andrushchenko.apps.recyclerview.tasks

sealed class Result<T> {
    fun <R> map(mapper: (T) -> R): Result<R> {
        if (this is Success) return Success(mapper(data))
        return this as Result<R>
    }

    class Success<T>(val data: T) : Result<T>()

    class Error<T>(val exception: Throwable) : Result<T>()

    class Pending<T> : Result<T>()

    class Empty<T> : Result<T>()
}

