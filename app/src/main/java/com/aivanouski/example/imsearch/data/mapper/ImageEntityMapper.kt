package com.aivanouski.example.imsearch.data.mapper

import com.aivanouski.example.imsearch.data.api.entity.ImageEntity
import com.aivanouski.example.imsearch.model.Image

class ImageEntityMapper {

    fun map(entities: List<ImageEntity>): List<Image> {
        return entities.map { entity -> map(entity) }
    }

    fun map(entity: ImageEntity): Image {
        return Image(
            id = entity.id,
            url = entity.url,
            previewUrl = entity.previewUrl,
            username = entity.username,
            userImageUrl = entity.userImageUrl,
            tags = entity.tags,
            likes = entity.likes,
            downloads = entity.downloads,
            comments = entity.comments
        )
    }
}