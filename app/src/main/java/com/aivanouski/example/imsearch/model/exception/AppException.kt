package com.aivanouski.example.imsearch.model.exception

open class AppException(
    message: String? = null,
    cause: Exception? = null
) : Exception(message, cause)