package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import com.example.glimpse.core.network.service.AuthApiService

actual fun createPlatformAuthRepository(
    authApiService: AuthApiService,
    tokenStorage: TokenStorage,
): AuthRepository = IosClerkBridgeRequiredAuthRepository(tokenStorage)

private class IosClerkBridgeRequiredAuthRepository(
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun isSignedIn(): Boolean = tokenStorage.getToken() != null

    override suspend fun signIn(email: String, password: String): ScreenState<Unit> =
        ScreenState.Error(IOS_CLERK_BRIDGE_REQUIRED_MESSAGE)

    override suspend fun signUp(email: String, password: String): ScreenState<SignUpOutcome> =
        ScreenState.Error(IOS_CLERK_BRIDGE_REQUIRED_MESSAGE)

    override suspend fun verifyEmail(signUpId: String, code: String): ScreenState<Unit> =
        ScreenState.Error(IOS_CLERK_BRIDGE_REQUIRED_MESSAGE)

    override suspend fun signOut(): ScreenState<Unit> {
        tokenStorage.clearToken()
        return ScreenState.Success(Unit)
    }
}

private const val IOS_CLERK_BRIDGE_REQUIRED_MESSAGE =
    "iOS auth requires the ClerkKit bridge. Android native Clerk auth is configured; iOS is intentionally not using the insecure direct Frontend API path."
