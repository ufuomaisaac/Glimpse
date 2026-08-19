package com.example.glimpse.core.data.storage

import com.example.glimpse.core.network.TokenProvider
import org.koin.dsl.module

actual val storageModule = module {
    single { DataStoreTokenStorage() }
    single<TokenStorage> { get<DataStoreTokenStorage>() }
    single<UserStorage> { get<DataStoreTokenStorage>() }
    single<TokenProvider> { get<DataStoreTokenStorage>() }
}
