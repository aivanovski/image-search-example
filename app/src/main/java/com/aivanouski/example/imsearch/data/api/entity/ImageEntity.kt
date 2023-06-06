package com.aivanouski.example.imsearch.data.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageEntity(
    @Json(name = "id")
    val id: Long,

    @Json(name = "largeImageURL")
    val url: String,

    @Json(name = "webformatURL")
    val previewUrl: String,

    @Json(name = "user")
    val username: String,

    @Json(name = "userImageURL")
    val userImageUrl: String,

    @Json(name = "tags")
    val tags: String,

    @Json(name = "likes")
    val likes: Int,

    @Json(name = "downloads")
    val downloads: Int,

    @Json(name = "comments")
    val comments: Int
)