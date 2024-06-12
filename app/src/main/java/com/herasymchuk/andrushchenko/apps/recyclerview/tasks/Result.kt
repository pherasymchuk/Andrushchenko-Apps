package com.herasymchuk.andrushchenko.apps.recyclerview.tasks

sealed class Result<T>

class SuccessResult<T>(val data: T) : Result<T>()

class ErrorResult<T>(val exception: Throwable) : Result<T>()

class PendingResult<T> : Result<T>()

class EmptyResult<T> : Result<T>()
