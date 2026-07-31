package com.example.glimpse.feature.upload.di

import com.example.glimpse.feature.upload.viewmodel.CreateFirstEventViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uploadModule = module {
    viewModelOf(::CreateFirstEventViewModel)
}
