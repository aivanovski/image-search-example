package com.aivanouski.example.imsearch.presentation.core

import androidx.lifecycle.ViewModel
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BaseCoroutineViewModel(dispatchers: DispatcherProvider) : ViewModel() {

    private val job = SupervisorJob()
    protected val viewModelScope = CoroutineScope(dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}