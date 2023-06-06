package com.aivanouski.example.imsearch.domain

import com.aivanouski.example.imsearch.R
import com.aivanouski.example.imsearch.model.Result
import java.io.IOException

class ErrorInteractor(
    private val resourceProvider: ResourceProvider
) {

    fun getMessage(error: Result.Error): String {
        return when (error.exception) {
            is IOException -> {
                resourceProvider.getString(R.string.please_check_your_internet_connection)
            }

            else -> resourceProvider.getString(R.string.error_has_been_occurred)
        }
    }
}