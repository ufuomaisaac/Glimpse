package com.example.glimpse.core.data

import com.clerk.api.Clerk
import com.clerk.api.auth.types.VerificationType
import com.clerk.api.network.model.error.ClerkErrorResponse
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.network.serialization.errorMessage
import com.clerk.api.signup.SignUp
import com.clerk.api.signup.sendCode
import com.clerk.api.signup.verifyCode
import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_sign_in_no_session
import glimpse.shared.generated.resources.error_sign_up_status
import glimpse.shared.generated.resources.error_verification_no_session
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.getString

class ClerkAndroidAuthRepository(
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun isSignedIn(): Boolean {
        awaitClerkInitialized()
        return Clerk.isSignedIn || tokenStorage.getToken() != null
    }

    override suspend fun signIn(email: String, password: String): ScreenState<Unit> {
        awaitClerkInitialized()
        return when (val result = Clerk.auth.signInWithPassword {
            identifier = email
            this.password = password
        }) {
            is ClerkResult.Success -> saveCurrentToken()
            is ClerkResult.Failure -> ScreenState.Error(result.authErrorMessage)
        }
    }

    override suspend fun signUp(email: String, password: String): ScreenState<SignUpOutcome> {
        awaitClerkInitialized()
        return when (val result = Clerk.auth.signUp {
            this.email = email
            this.password = password
        }) {
            is ClerkResult.Success -> handleSignUpResult(result.value, email)
            is ClerkResult.Failure -> ScreenState.Error(result.authErrorMessage)
        }
    }

    override suspend fun verifyEmail(signUpId: String, code: String): ScreenState<Unit> {
        awaitClerkInitialized()
        val signUp = Clerk.auth.currentSignUp
            ?: return ScreenState.Error(getString(Res.string.error_verification_no_session))

        return when (val result = signUp.verifyCode(code, VerificationType.EMAIL)) {
            is ClerkResult.Success -> saveCurrentToken()
            is ClerkResult.Failure -> ScreenState.Error(result.authErrorMessage)
        }
    }

    override suspend fun signOut(): ScreenState<Unit> {
        awaitClerkInitialized()
        Clerk.auth.signOut()
        tokenStorage.clearToken()
        return ScreenState.Success(Unit)
    }

    private suspend fun handleSignUpResult(
        signUp: SignUp,
        email: String,
    ): ScreenState<SignUpOutcome> = when (signUp.status) {
        SignUp.Status.COMPLETE -> saveCurrentToken().mapSuccess { SignUpOutcome.Complete }
        SignUp.Status.MISSING_REQUIREMENTS -> sendEmailVerificationCode(signUp, email)
        else -> ScreenState.Error(
            getString(Res.string.error_sign_up_status, signUp.status.name.lowercase())
        )
    }

    private suspend fun sendEmailVerificationCode(
        signUp: SignUp,
        email: String,
    ): ScreenState<SignUpOutcome> = when (val result = signUp.sendCode { this.email = email }) {
        is ClerkResult.Success -> ScreenState.Success(SignUpOutcome.NeedsEmailVerification(signUp.id))
        is ClerkResult.Failure -> ScreenState.Error(result.authErrorMessage)
    }

    private suspend fun saveCurrentToken(): ScreenState<Unit> = when (val tokenResult = Clerk.auth.getToken()) {
        is ClerkResult.Success -> {
            tokenStorage.saveToken(tokenResult.value)
            ScreenState.Success(Unit)
        }
        is ClerkResult.Failure -> ScreenState.Error(
            tokenResult.authErrorMessage.takeIf { it.isNotBlank() }
                ?: getString(Res.string.error_sign_in_no_session)
        )
    }

    private suspend fun awaitClerkInitialized() {
        Clerk.isInitialized.filter { it }.first()
    }

    private inline fun <T, R> ScreenState<T>.mapSuccess(transform: (T) -> R): ScreenState<R> =
        when (this) {
            is ScreenState.Success -> ScreenState.Success(transform(data))
            is ScreenState.Error -> this
            ScreenState.Loading -> ScreenState.Loading
        }
}

private val ClerkResult.Failure<ClerkErrorResponse>.authErrorMessage: String
    get() = throwable?.message ?: errorMessage
