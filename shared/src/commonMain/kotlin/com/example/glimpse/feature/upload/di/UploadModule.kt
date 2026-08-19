package com.example.glimpse.feature.upload.di

import com.example.glimpse.core.data.UploadRepository
import com.example.glimpse.core.data.UploadRepositoryImpl
import com.example.glimpse.feature.upload.viewmodel.CreateFirstEventViewModel
import com.example.glimpse.feature.upload.viewmodel.CreateEventViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uploadModule = module {
    single<UploadRepository> { UploadRepositoryImpl(get()) }
    viewModelOf(::CreateFirstEventViewModel)
    viewModelOf(::CreateEventViewModel)
}
