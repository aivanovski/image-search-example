package com.aivanouski.example.imsearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aivanouski.example.imsearch.data.db.dao.ImageDao
import com.aivanouski.example.imsearch.data.db.dao.QueryDao
import com.aivanouski.example.imsearch.data.db.dao.QueryToImageRelationDao
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Query
import com.aivanouski.example.imsearch.model.QueryToImageRelation

@Database(
    entities = [Image::class, Query::class, QueryToImageRelation::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
    abstract fun queryDao(): QueryDao
    abstract fun queryToImageRelationDao(): QueryToImageRelationDao

    companion object {
        const val DB_NAME = "images.db"
    }
}