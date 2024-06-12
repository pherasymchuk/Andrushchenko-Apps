package com.herasymchuk.andrushchenko.apps.recyclerview.tasks

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

private val handler = Handler(Looper.getMainLooper())

/**
 * A default implementation of the [Task] interface.
 *
 * This class executes a [Callable] task asynchronously and provides methods to register callbacks
 * for success and error results.
 *
 * @param T The type of the result.
 * @param callable The callable task to execute.
 */
class CallableTask<T>(
    private val executorService: ExecutorService = Executors.newCachedThreadPool(),
    private val callable: Callable<T>,
) : Task<T> {
    private val future: Future<*>
    private var result: Result<T> = Result.Pending()

    init {
        future = executorService.submit {
            result = try {
                val data: T = callable.call()
                Result.Success(data)
            } catch (e: Throwable) {
                Result.Error(e)
            }
            notifyListeners()
        }
    }

    private var successCallback: Callback<T>? = null
    private var errorCallback: Callback<Throwable>? = null

    override fun onSuccess(callback: Callback<T>): Task<T> {
        this.successCallback = callback
        notifyListeners()
        return this
    }

    override fun onError(callback: Callback<Throwable>): Task<T> {
        this.errorCallback = callback
        notifyListeners()
        return this
    }

    override fun cancel() {
        future.cancel(true)
        clear()
    }

    /**
     * Awaits the completion of the future and returns the result.
     *
     * If the future completes successfully, the result is returned.
     * If the future completes with an error, the exception is thrown.
     *
     * @return The result of the future.
     * @throws Exception The exception thrown by the future.
     */
    override fun await(): T {
        future.get()
        return when (result) { // 6. Use 'when' for more concise result handling
            is Result.Success -> (result as Result.Success<T>).data
            is Result.Error -> throw (result as Result.Error<T>).exception
            else -> throw IllegalStateException("Task is not completed") // 7. Handle unexpected state
        }
    }

    /**
     * Notifies the listeners about the result of the operation.
     *
     * This method is called on the main thread to ensure that the callbacks are executed in the correct context.
     *
     * - If the result is a [Result.Success], the [successCallback] is invoked with the result data.
     * - If the result is a [Result.Error], the [errorCallback] is invoked with the exception.
     *
     * After the callbacks are executed, the result and the callbacks are cleared.
     */
    private fun notifyListeners() {
        handler.post {
            val result = this.result
            val successCallback = this.successCallback
            val errorCallback = this.errorCallback

            when {
                result is Result.Success && successCallback != null -> {
                    successCallback(result.data)
                    clear()
                }

                result is Result.Error && errorCallback != null -> {
                    errorCallback(result.exception)
                    clear()
                }
            }
        }
    }

    private fun clear() {
        successCallback = null
        errorCallback = null
    }

}
