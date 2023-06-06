package com.aivanouski.example.imsearch.data.api

import com.aivanouski.example.imsearch.model.Resource
import com.aivanouski.example.imsearch.model.exception.ApiException
import java.io.IOException
import retrofit2.Response
import timber.log.Timber

abstract class BaseRemoteDataSource {

    protected suspend fun <T : Any> request(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    return Resource.Success(data)
                }
            }

            val code = response.code()
            val message = response.message()

            return Resource.Error(
                ApiException(message = "Unknown error: status=$code, message=$message")
            )
        } catch (e: IOException) {
            Timber.d(e)
            return Resource.Error(e)
        } catch (e: Exception) {
            Timber.d(e)
            return Resource.Error(ApiException(cause = e))
        }
    }
}