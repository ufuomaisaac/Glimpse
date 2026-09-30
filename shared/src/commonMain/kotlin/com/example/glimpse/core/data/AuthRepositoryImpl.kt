package com.example.glimpse.core.data

import com.example.glimpse.core.common.DataState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import com.example.glimpse.core.model.User
import com.example.glimpse.core.network.mapper.toDomain
import com.example.glimpse.core.network.service.AuthApiService
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_google_sign_in_unavailable
import glimpse.shared.generated.resources.error_sign_in_failed
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
    private val userRepository: UserRepository,
) : AuthRepository {

    private var pendingUser: User? = null

    override suspend fun isSignedIn(): Boolean {
        val signedIn = tokenStorage.getToken() != null
        if (signedIn) userRepository.getUser()
        return signedIn
    }

    override suspend fun signIn(email: String, password: String): DataState<Unit> = try {
        val session = authApiService.signIn(email, password).toDomain()
            ?: return DataState.Error(getString(Res.string.error_sign_in_no_session))

        tokenStorage.saveToken(session.token)
        userRepository.getUser()
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: getString(Res.string.error_sign_in_failed))
    }

    override suspend fun continueWithGoogle(): DataState<Unit> =
        DataState.Error(getString(Res.string.error_google_sign_in_unavailable))

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
    ): DataState<SignUpOutcome> = try {
        val response = authApiService.signUp(email, password, username)
        val user = User(displayName = username, email = email)
        when {
            response.response.status == STATUS_COMPLETE -> {
                val session = response.toDomain()
                    ?: return DataState.Error(getString(Res.string.error_sign_up_no_session))
                tokenStorage.saveToken(session.token)
                userRepository.setUser(user)
                pendingUser = null
                DataState.Success(SignUpOutcome.Complete)
            }
            FIELD_EMAIL_ADDRESS in response.response.unverifiedFields -> {
                pendingUser = user
                authApiService.prepareEmailVerification(response.response.id)
                DataState.Success(SignUpOutcome.NeedsEmailVerification(response.response.id))
            }
            else -> DataState.Error(
                getString(Res.string.error_sign_up_status, response.response.status),
            )
        }
    } catch (e: Exception) {
        DataState.Error(e.message ?: getString(Res.string.error_sign_up_failed))
    }

    override suspend fun verifyEmail(signUpId: String, code: String): DataState<Unit> = try {
        val session = authApiService.verifyEmail(signUpId, code).toDomain()
            ?: return DataState.Error(getString(Res.string.error_verification_no_session))
        tokenStorage.saveToken(session.token)
        pendingUser?.let { userRepository.setUser(it) }
        pendingUser = null
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: getString(Res.string.error_verification_failed))
    }

    override suspend fun signOut(): DataState<Unit> = try {
        tokenStorage.clearToken()
        userRepository.clearUser()
        pendingUser = null
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: getString(Res.string.error_sign_out_failed))
    }

    private companion object {
        const val STATUS_COMPLETE = "complete"
        const val FIELD_EMAIL_ADDRESS = "email_address"
    }
}


