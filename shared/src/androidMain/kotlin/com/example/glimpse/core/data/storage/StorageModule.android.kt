package com.example.glimpse.core.data.storage

import com.example.glimpse.core.network.TokenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val storageModule = module {
    single { DataStoreTokenStorage(androidContext()) }
    single<TokenStorage> { get<DataStoreTokenStorage>() }
    single<TokenProvider> { get<DataStoreTokenStorage>() }
}
