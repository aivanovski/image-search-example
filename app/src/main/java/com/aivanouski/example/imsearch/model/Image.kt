package com.aivanouski.example.imsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image")
data class Image(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "preview_url")
    val previewUrl: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "user_image_url")
    val userImageUrl: String,

    @ColumnInfo(name = "rags")
    val tags: String,

    @ColumnInfo(name = "likes")
    val likes: Int,

    @ColumnInfo(name = "downloads")
    val downloads: Int,

    @ColumnInfo(name = "comments")
    val comments: Int
)