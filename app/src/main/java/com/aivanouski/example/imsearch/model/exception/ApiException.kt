package com.aivanouski.example.imsearch.model.exception

class ApiException(
    message: String? = null,
    cause: Exception? = null
) : AppException(message, cause)