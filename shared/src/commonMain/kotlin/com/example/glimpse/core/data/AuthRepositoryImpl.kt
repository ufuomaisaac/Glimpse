package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import com.example.glimpse.core.network.mapper.toDomain
import com.example.glimpse.core.network.service.AuthApiService

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): ScreenState<Unit> = try {
        val session = authApiService.signIn(email, password).toDomain()
            ?: return ScreenState.Error("Sign in failed: no session in response")
        tokenStorage.saveToken(session.token)
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: "Sign in failed")
    }

    override suspend fun signUp(email: String, password: String): ScreenState<SignUpOutcome> = try {
        val response = authApiService.signUp(email, password)
        when {
            response.response.status == "complete" -> {
                val session = response.toDomain()
                    ?: return ScreenState.Error("Sign up failed: no session in response")
                tokenStorage.saveToken(session.token)
                ScreenState.Success(SignUpOutcome.Complete)
            }
            response.response.unverifiedFields.contains("email_address") -> {
                authApiService.prepareEmailVerification(response.response.id)
                ScreenState.Success(SignUpOutcome.NeedsEmailVerification(response.response.id))
            }
            else -> ScreenState.Error("Sign up failed: ${response.response.status}")
        }
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: "Sign up failed")
    }

    override suspend fun verifyEmail(signUpId: String, code: String): ScreenState<Unit> = try {
        val session = authApiService.verifyEmail(signUpId, code).toDomain()
            ?: return ScreenState.Error("Verification failed: no session in response")
        tokenStorage.saveToken(session.token)
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: "Email verification failed")
    }

    override suspend fun signOut(): ScreenState<Unit> = try {
        tokenStorage.clearToken()
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: "Sign out failed")
    }
}
