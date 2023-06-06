package com.aivanouski.example.imsearch.testUtils

import com.aivanouski.example.imsearch.presentation.core.coroutines.CoroutineJobFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NeverExecutedCoroutineJobFactory : CoroutineJobFactory {

    override fun launchDelayed(
        scope: CoroutineScope,
        delayInMillis: Long,
        action: () -> Unit
    ): Job {
        // action will never be called
        return scope.launch {
        }
    }
}