package com.aivanouski.example.imsearch.data.api

import com.aivanouski.example.imsearch.data.api.entity.ImageResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

    @GET("api")
    suspend fun getImages(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Response<ImageResponseEntity>

    @GET("api")
    suspend fun getImage(
        @Query("id") id: Long,
        @Query("key") apiKey: String
    ): Response<ImageResponseEntity>
}