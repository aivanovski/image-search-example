package com.aivanouski.example.imsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aivanouski.example.imsearch.model.QueryToImageRelation

@Dao
interface QueryToImageRelationDao {

    @Query("SELECT * FROM QueryToImageRelation WHERE query_id = :queryId")
    fun getByQueryId(queryId: Long): List<QueryToImageRelation>

    @Insert
    fun insert(relations: List<QueryToImageRelation>)

    @Query("DELETE FROM QueryToImageRelation WHERE query_id = :queryId")
    fun deleteByQueryId(queryId: Long)
}