package com.aivanouski.example.imsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "QueryToImageRelation")
data class QueryToImageRelation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,

    @ColumnInfo(name = "query_id")
    val queryId: Long,

    @ColumnInfo(name = "image_id")
    val imageId: Long
)