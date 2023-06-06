package com.aivanouski.example.imsearch.di

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.qualifier.Qualifier

object Injector {

    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null
    ): Lazy<T> = GlobalContext.get().koin.inject(qualifier)

    inline fun <reified T : Any> get(
        params: DefinitionParameters? = null
    ): T = GlobalContext.get().koin.get(
        null,
        parameters = if (params != null) {
            { params }
        } else {
            null
        }
    )
}