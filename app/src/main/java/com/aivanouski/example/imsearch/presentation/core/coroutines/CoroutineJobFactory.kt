package com.aivanouski.example.imsearch.presentation.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface CoroutineJobFactory {
    fun launchDelayed(
        scope: CoroutineScope,
        delayInMillis: Long,
        action: () -> Unit
    ): Job
}