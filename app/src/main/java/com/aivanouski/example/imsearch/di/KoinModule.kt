package com.aivanouski.example.imsearch.di

import android.content.Context
import androidx.room.Room
import com.aivanouski.example.imsearch.data.api.Api
import com.aivanouski.example.imsearch.data.api.ImageRemoteDataSource
import com.aivanouski.example.imsearch.data.api.ImageService
import com.aivanouski.example.imsearch.data.db.AppDatabase
import com.aivanouski.example.imsearch.data.db.dao.ImageDao
import com.aivanouski.example.imsearch.data.mapper.ImageEntityMapper
import com.aivanouski.example.imsearch.data.repository.ImageCache
import com.aivanouski.example.imsearch.data.repository.ImageRepository
import com.aivanouski.example.imsearch.domain.ErrorInteractor
import com.aivanouski.example.imsearch.domain.ResourceProvider
import com.aivanouski.example.imsearch.domain.ResourceProviderImpl
import com.aivanouski.example.imsearch.domain.imageDetails.ImageDetailsInteractor
import com.aivanouski.example.imsearch.domain.images.ImagesInteractor
import com.aivanouski.example.imsearch.presentation.MainViewModel
import com.aivanouski.example.imsearch.presentation.core.coroutines.CoroutineJobFactory
import com.aivanouski.example.imsearch.presentation.core.coroutines.CoroutineJobFactoryImpl
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProviderImpl
import com.aivanouski.example.imsearch.presentation.imageDetails.ImageDetailsArguments
import com.aivanouski.example.imsearch.presentation.imageDetails.ImageDetailsViewModel
import com.aivanouski.example.imsearch.presentation.images.ImagesViewModel
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImageItemFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object KoinModule {

    val appModule = module {
        single<ResourceProvider> { ResourceProviderImpl(get()) }
        single { ErrorInteractor(get()) }
        single<CoroutineJobFactory> { CoroutineJobFactoryImpl() }
        single<DispatcherProvider> { DispatcherProviderImpl() }

        // Database
        single { provideDatabase(get()) }
        single { provideImageDao(get()) }

        // Network
        single { provideOkHttp() }
        single { provideRetrofit(get()) }
        single { providerImageRemoteDataSource(get()) }

        // Repository
        single { ImageEntityMapper() }
        single { ImageCache(get()) }
        single { ImageRepository(get(), get(), get(), get()) }

        // Navigation
        single { Cicerone.create() }
        single { provideNavigationRouter(get()) }
        single { provideNavigatorHolder(get()) }

        // Main screen
        viewModel { MainViewModel(get()) }

        // Images screen
        single { ImagesInteractor(get()) }
        single { ImageItemFactory() }
        viewModel {
            ImagesViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                ImagesViewModel.DEFAULT_CONFIG
            )
        }

        // Image Details screen
        single { ImageDetailsInteractor(get()) }
        factory { (args: ImageDetailsArguments) ->
            ImageDetailsViewModel(
                get(),
                get(),
                get(),
                args
            )
        }
    }

    private fun provideOkHttp(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Api.SERVER_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
    }

    private fun provideImageDao(db: AppDatabase): ImageDao =
        db.imageDao()

    private fun providerImageRemoteDataSource(retrofit: Retrofit): ImageRemoteDataSource {
        val service = retrofit.create(ImageService::class.java)
        return ImageRemoteDataSource(service)
    }

    private fun provideNavigationRouter(cicerone: Cicerone<Router>): Router =
        cicerone.router

    private fun provideNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.getNavigatorHolder()
}