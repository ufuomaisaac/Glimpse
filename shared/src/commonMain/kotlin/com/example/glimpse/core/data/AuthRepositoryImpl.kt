package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import com.example.glimpse.core.network.mapper.toDomain
import com.example.glimpse.core.network.service.AuthApiService
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_sign_in_failed
import glimpse.shared.generated.resources.error_google_sign_in_unavailable
import glimpse.shared.generated.resources.error_sign_in_no_session
import glimpse.shared.generated.resources.error_sign_out_failed
import glimpse.shared.generated.resources.error_sign_up_failed
import glimpse.shared.generated.resources.error_sign_up_no_session
import glimpse.shared.generated.resources.error_sign_up_status
import glimpse.shared.generated.resources.error_verification_failed
import glimpse.shared.generated.resources.error_verification_no_session
import org.jetbrains.compose.resources.getString

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun isSignedIn(): Boolean = tokenStorage.getToken() != null

    override suspend fun signIn(email: String, password: String): ScreenState<Unit> = try {
        val session = authApiService.signIn(email, password).toDomain()
            ?: return ScreenState.Error(getString(Res.string.error_sign_in_no_session))
        tokenStorage.saveToken(session.token)
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: getString(Res.string.error_sign_in_failed))
    }

    override suspend fun continueWithGoogle(): ScreenState<Unit> =
        ScreenState.Error(getString(Res.string.error_google_sign_in_unavailable))

    override suspend fun signUp(email: String, password: String, username: String): ScreenState<SignUpOutcome> = try {
        val response = authApiService.signUp(email, password, username)
        when {
            response.response.status == "complete" -> {
                val session = response.toDomain()
                    ?: return ScreenState.Error(getString(Res.string.error_sign_up_no_session))
                tokenStorage.saveToken(session.token)
                ScreenState.Success(SignUpOutcome.Complete)
            }
            response.response.unverifiedFields.contains("email_address") -> {
                authApiService.prepareEmailVerification(response.response.id)
                ScreenState.Success(SignUpOutcome.NeedsEmailVerification(response.response.id))
            }
            else -> ScreenState.Error(
                getString(Res.string.error_sign_up_status, response.response.status)
            )
        }
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: getString(Res.string.error_sign_up_failed))
    }

    override suspend fun verifyEmail(signUpId: String, code: String): ScreenState<Unit> = try {
        val session = authApiService.verifyEmail(signUpId, code).toDomain()
            ?: return ScreenState.Error(getString(Res.string.error_verification_no_session))
        tokenStorage.saveToken(session.token)
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: getString(Res.string.error_verification_failed))
    }

    override suspend fun signOut(): ScreenState<Unit> = try {
        tokenStorage.clearToken()
        ScreenState.Success(Unit)
    } catch (e: Exception) {
        ScreenState.Error(e.message ?: getString(Res.string.error_sign_out_failed))
    }
}
