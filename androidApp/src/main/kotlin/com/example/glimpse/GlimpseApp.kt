package com.example.glimpse

import android.app.Application
import com.example.glimpse.core.data.storage.storageModule
import com.example.glimpse.core.network.di.networkModule
import com.example.glimpse.feature.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GlimpseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GlimpseApp)
            modules(
                storageModule,
                networkModule,
                authModule,
            )
        }
    }
}
