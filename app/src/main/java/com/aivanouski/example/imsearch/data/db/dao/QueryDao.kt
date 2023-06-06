package com.aivanouski.example.imsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.aivanouski.example.imsearch.model.Query

@Dao
interface QueryDao {

    @androidx.room.Query("SELECT * FROM `Query` WHERE `query` = :query")
    fun find(query: String): Query?

    @Insert
    fun insert(query: Query): Long
}