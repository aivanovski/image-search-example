package com.aivanouski.example.imsearch.testUtils

import androidx.lifecycle.LiveData

class LiveDataValueCaptor<T>(liveData: LiveData<T>) {

    private val _capturedValues = mutableListOf<T>()
    val capturedValues: List<T> = _capturedValues

    init {
        liveData.observeForever { value ->
            _capturedValues.add(value)
        }
    }
}