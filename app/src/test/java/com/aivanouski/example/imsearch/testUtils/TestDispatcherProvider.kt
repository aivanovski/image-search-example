package com.aivanouski.example.imsearch.testUtils

import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider : DispatcherProvider {
    override val Main: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val IO: CoroutineDispatcher = UnconfinedTestDispatcher()
}