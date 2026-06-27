package com.example.glimpse.feature.auth.di

import com.example.glimpse.core.data.AuthRepository
import com.example.glimpse.core.data.AuthRepositoryImpl
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModelOf(::AuthViewModel)
}
