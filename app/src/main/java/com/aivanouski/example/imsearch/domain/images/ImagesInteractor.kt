package com.aivanouski.example.imsearch.domain.images

import com.aivanouski.example.imsearch.data.repository.ImageRepository
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Result
import kotlinx.coroutines.flow.Flow

class ImagesInteractor(
    private val repository: ImageRepository
) {

    fun getImages(query: String): Flow<Result<List<Image>>> =
        repository.getImages(query)
}