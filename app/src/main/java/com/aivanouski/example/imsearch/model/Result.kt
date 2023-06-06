package com.aivanouski.example.imsearch.model

sealed class Result<out T : Any?> {

    data class Success<out T : Any?>(val data: T) : Result<T>()
    data class Cached<out T : Any?>(val data: T, val exception: Exception? = null) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    fun unwrap(): T {
        return when (this) {
            is Success -> data
            is Cached -> data
            else -> throw IllegalStateException()
        }
    }

    fun unwrapError(): Error = (this as Error)

    fun isFailed(): Boolean = (this is Error)

    fun isSucceeded(): Boolean = (this is Success || this is Cached)

    fun isCached(): Boolean = (this is Cached)

    fun <T> mapWith(newData: T): Result<T> {
        return if (isSucceeded()) {
            Success(newData)
        } else {
            unwrapError()
        }
    }
}