package com.aivanouski.example.imsearch.data.repository

import com.aivanouski.example.imsearch.data.api.ImageRemoteDataSource
import com.aivanouski.example.imsearch.data.mapper.ImageEntityMapper
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Resource
import com.aivanouski.example.imsearch.model.Result
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImageRepository(
    private val cache: ImageCache,
    private val api: ImageRemoteDataSource,
    private val entityMapper: ImageEntityMapper,
    private val dispatchers: DispatcherProvider
) {

    fun getImages(query: String): Flow<Result<List<Image>>> {
        return flow {
            val local = cache.getAllByQuery(query)
            if (local.isNotEmpty()) {
                emit(Result.Cached(local))
            }

            when (val response = api.getImages(query)) {
                is Resource.Success -> {
                    val remote = entityMapper.map(response.data)
                    cache.save(query, remote)
                    emit(Result.Success(remote))
                }

                is Resource.Error -> {
                    if (local.isNotEmpty()) {
                        emit(Result.Cached(local, response.exception))
                    } else {
                        emit(Result.Error(response.exception))
                    }
                }
            }
        }
            .flowOn(dispatchers.IO)
    }

    fun getImageById(id: Long): Flow<Result<Image>> {
        return flow {
            val local = cache.getById(id)

            when (val response = api.getImage(id)) {
                is Resource.Success -> {
                    val remote = entityMapper.map(response.data)
                    cache.save(remote)
                    emit(Result.Success(remote))
                }

                is Resource.Error -> {
                    if (local != null) {
                        emit(Result.Cached(local, response.exception))
                    } else {
                        emit(Result.Error(response.exception))
                    }
                }
            }
        }
            .flowOn(dispatchers.IO)
    }
}