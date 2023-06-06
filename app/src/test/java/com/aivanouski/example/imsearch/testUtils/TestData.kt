package com.aivanouski.example.imsearch.testUtils

import com.aivanouski.example.imsearch.model.Image

object TestData {

    const val DEFAULT_QUERY = "default-query"
    const val DELAY = 123L
    const val QUERY = "test-query"
    const val QUERY1 = "test-query1"
    const val QUERY2 = "test-query2"
    const val ERROR_MESSAGE = "test-error-message"

    val IMAGE1 = Image(
        id = 1,
        url = "url1",
        previewUrl = "previewUrl1",
        username = "username1",
        userImageUrl = "userImageUrl1",
        tags = "tag1, tag2",
        likes = 101,
        downloads = 102,
        comments = 103
    )

    val IMAGE2 = Image(
        id = 2,
        url = "url2",
        previewUrl = "previewUrl2",
        username = "username2",
        userImageUrl = "userImageUrl2",
        tags = "tag3, tag4",
        likes = 201,
        downloads = 202,
        comments = 203
    )

    val IMAGES = listOf(IMAGE1, IMAGE2)
}