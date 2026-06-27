package com.example.glimpse.core.network.di

import com.example.glimpse.core.network.createAuthHttpClient
import com.example.glimpse.core.network.createHttpClient
import com.example.glimpse.core.network.service.AuthApiService
import com.example.glimpse.core.network.service.ClusterApiService
import com.example.glimpse.core.network.service.UploadApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("authClient")) { createAuthHttpClient() }
    single(named("appClient")) { createHttpClient(get()) }
    single { AuthApiService(get(named("authClient"))) }
    single { UploadApiService(get(named("appClient"))) }
    single { ClusterApiService(get(named("appClient"))) }
}
