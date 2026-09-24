package com.example.glimpse.core.network.di

import com.example.glimpse.core.network.createBackendHttpClient
import com.example.glimpse.core.network.createClerkHttpClient
import com.example.glimpse.core.network.service.AuthApiService
import com.example.glimpse.core.network.service.UploadApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("clerkClient")) { createClerkHttpClient() }
    single(named("backendClient")) { createBackendHttpClient(get()) }
    single { AuthApiService(get(named("clerkClient"))) }
    single { UploadApiService(get(named("backendClient"))) }
}
