package com.example.glimpse

import android.app.Application
import com.clerk.api.Clerk
import com.example.glimpse.core.network.ClerkConfig
import com.example.glimpse.core.data.storage.storageModule
import com.example.glimpse.core.network.di.networkModule
import com.example.glimpse.feature.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GlimpseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Clerk.initialize(
            this,
            publishableKey = ClerkConfig.PUBLISHABLE_KEY,
        )
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
