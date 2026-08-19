package com.example.glimpse.core.network

import co.touchlab.kermit.Logger
import com.clerk.api.Clerk
import com.clerk.api.network.serialization.ClerkResult
import com.example.glimpse.core.data.storage.TokenStorage

class ClerkTokenProvider(
    private val tokenStorage: TokenStorage,
) : TokenProvider {

    private val logger = Logger.withTag("HttpAuth")

    override suspend fun getToken(): String? {
        if (!Clerk.isSignedIn) {
            return tokenStorage.getToken()
        }

        return when (val result = Clerk.auth.getToken()) {
            is ClerkResult.Success -> result.value.also { token ->
                tokenStorage.saveToken(token)
                logger.d { "Refreshed Clerk token for authenticated API request" }
            }

            is ClerkResult.Failure -> {
                logger.w { "Could not refresh Clerk token; using the stored token" }
                tokenStorage.getToken()
            }
        }
    }
}
