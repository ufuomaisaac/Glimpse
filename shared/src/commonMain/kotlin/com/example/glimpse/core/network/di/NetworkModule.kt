package com.example.glimpse.core.network.di

import com.example.glimpse.core.network.createHttpClient
import com.example.glimpse.core.network.service.ClusterApiService
import com.example.glimpse.core.network.service.UploadApiService
import org.koin.dsl.module

val networkModule = module {
    // TokenProvider is bound by the auth module — get() resolves it at runtime
    single { createHttpClient(get()) }

    single { UploadApiService(get()) }
    single { ClusterApiService(get()) }
}
