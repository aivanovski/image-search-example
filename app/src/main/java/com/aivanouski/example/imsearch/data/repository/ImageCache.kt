package com.aivanouski.example.imsearch.data.repository

import com.aivanouski.example.imsearch.data.db.AppDatabase
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Query
import com.aivanouski.example.imsearch.model.QueryToImageRelation

class ImageCache(
    private val db: AppDatabase
) {

    fun getAllByQuery(query: String): List<Image> {
        val queryId = db.queryDao().find(query)?.id ?: return emptyList()
        return db.imageDao().getAllByQueryId(queryId)
    }

    fun save(query: String, images: List<Image>) {
        val queryId = db.queryDao().find(query)?.id
            ?: db.queryDao().insert(Query(id = null, query = query))

        db.imageDao().insert(images)

        val relations = images.map { image ->
            QueryToImageRelation(
                id = null,
                queryId = queryId,
                imageId = image.id
            )
        }

        db.queryToImageRelationDao().deleteByQueryId(queryId)
        db.queryToImageRelationDao().insert(relations)
    }

    fun getById(imageId: Long): Image? {
        return db.imageDao().getById(imageId)
    }

    fun save(image: Image) {
        db.imageDao().deleteAndInsert(image)
    }
}