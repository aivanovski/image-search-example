package com.aivanouski.example.imsearch.domain.imageDetails

import com.aivanouski.example.imsearch.data.repository.ImageRepository
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Result
import kotlinx.coroutines.flow.Flow

class ImageDetailsInteractor(
    private val repository: ImageRepository
) {

    fun getImage(id: Long): Flow<Result<Image>> =
        repository.getImageById(id)
}