package com.aivanouski.example.imsearch.data.api

import com.aivanouski.example.imsearch.data.api.entity.ImageEntity
import com.aivanouski.example.imsearch.model.Resource
import com.aivanouski.example.imsearch.model.exception.ApiException

class ImageRemoteDataSource(
    private val service: ImageService
) : BaseRemoteDataSource() {

    suspend fun getImages(query: String): Resource<List<ImageEntity>> {
        val response = request {
            service.getImages(
                query = query,
                apiKey = Api.API_KEY
            )
        }

        return response.map { data -> data.items }
    }

    suspend fun getImage(id: Long): Resource<ImageEntity> {
        val response = request {
            service.getImage(
                id = id,
                apiKey = Api.API_KEY
            )
        }

        return response.flatMap { data ->
            if (data.items.isNotEmpty()) {
                Resource.Success(data.items.first())
            } else {
                Resource.Error(ApiException("Unable to find image with id: $id"))
            }
        }
    }
}