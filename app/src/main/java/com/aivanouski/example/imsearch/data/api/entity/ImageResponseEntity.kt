package com.aivanouski.example.imsearch.data.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponseEntity(
    @Json(name = "hits")
    val items: List<ImageEntity>
)