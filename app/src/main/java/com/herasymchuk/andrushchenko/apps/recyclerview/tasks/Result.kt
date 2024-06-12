package com.herasymchuk.andrushchenko.apps.recyclerview.tasks

/**
 * A sealed class representing the result of an operation.
 *
 * It can be either a [Success] with the result data, an [Error] with an exception, a [Pending] indicating that the operation is still in progress, or an [Empty] result.
 *
 */
sealed class Result<T> {
    /**
     * Maps the value of this [Result] to a new value of type [R] using the provided [mapper] function.
     *
     * If this [Result] is a [Success], the [mapper] function is applied to the encapsulated data and the result is wrapped in a new [Success].
     * If this [Result] is a [Failure], it is returned unchanged.
     *
     * @param mapper The mapping function to apply to the data.
     * @return A new [Result] containing the mapped data or the original [Failure].
     */
    fun <R> map(mapper: (T) -> R): Result<R> {
        if (this is Success) return Success(mapper(data))
        return this as Result<R>
    }

    class Success<T>(val data: T) : Result<T>()

    class Error<T>(val exception: Throwable) : Result<T>()

    class Pending<T> : Result<T>()

    class Empty<T> : Result<T>()
}

