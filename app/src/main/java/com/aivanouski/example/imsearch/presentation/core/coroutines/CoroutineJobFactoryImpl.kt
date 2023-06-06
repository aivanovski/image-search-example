package com.aivanouski.example.imsearch.presentation.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineJobFactoryImpl : CoroutineJobFactory {

    override fun launchDelayed(
        scope: CoroutineScope,
        delayInMillis: Long,
        action: () -> Unit
    ): Job {
        return scope.launch {
            delay(delayInMillis)
            action.invoke()
        }
    }
}