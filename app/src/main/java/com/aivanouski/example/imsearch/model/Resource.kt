package com.aivanouski.example.imsearch.model

sealed class Resource<out T : Any?> {
    data class Error(val exception: Exception) : Resource<Nothing>()
    data class Success<out T : Any?>(val data: T) : Resource<T>()

    fun unwrap(): T = (this as Success).data

    fun asError(): Error = (this as Error)

    fun <R> map(transform: (data: T) -> R): Resource<R> {
        return when (this) {
            is Success -> Success(transform.invoke(data))
            is Error -> Error(exception)
        }
    }

    fun <R> flatMap(transform: (data: T) -> Resource<R>): Resource<R> {
        return when (this) {
            is Success -> transform.invoke(data)
            is Error -> Error(exception)
        }
    }
}