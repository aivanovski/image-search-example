package com.aivanouski.example.imsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aivanouski.example.imsearch.model.Image

@Dao
abstract class ImageDao {

    @Query("SELECT * FROM Image")
    abstract fun getAll(): List<Image>

    @Query(
        "SELECT * FROM Image INNER JOIN QueryToImageRelation " +
            "ON Image.id = QueryToImageRelation.image_id " +
            "WHERE QueryToImageRelation.query_id = :queryId"
    )
    abstract fun getAllByQueryId(queryId: Long): List<Image>

    @Query("SELECT * FROM Image WHERE id = :id")
    abstract fun getById(id: Long): Image?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(images: List<Image>)

    @Query("DELETE FROM Image WHERE id = :id")
    abstract fun deleteById(id: Long)

    @Transaction
    open fun deleteAndInsert(image: Image) {
        deleteById(image.id)
        insert(listOf(image))
    }
}