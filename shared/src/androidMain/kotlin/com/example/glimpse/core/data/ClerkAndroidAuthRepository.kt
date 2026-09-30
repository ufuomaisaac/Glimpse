package com.example.glimpse.core.data

import android.util.Log
import com.clerk.api.Clerk
import com.clerk.api.auth.types.VerificationType
import com.clerk.api.network.model.error.ClerkErrorResponse
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.signin.SignIn
import com.clerk.api.sso.OAuthResult
import com.clerk.api.signup.SignUp
import com.clerk.api.signup.sendEmailCode
import com.clerk.api.signup.verifyCode
import com.example.glimpse.core.common.DataState
import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.model.SignUpOutcome
import com.example.glimpse.core.model.User
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_sign_in_no_session
import glimpse.shared.generated.resources.error_sign_in_status
import glimpse.shared.generated.resources.error_sign_up_no_session
import glimpse.shared.generated.resources.error_sign_up_status
import glimpse.shared.generated.resources.error_verification_no_session
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.getString

class ClerkAndroidAuthRepository(
    private val tokenStorage: TokenStorage,
    private val userRepository: UserRepository,
) : AuthRepository {

    private var pendingUser: User? = null

    override suspend fun isSignedIn(): Boolean {
        val initializationError = ensureClerkReady()
        val signedIn = initializationError == null && Clerk.isSignedIn ||
            tokenStorage.getToken() != null
        if (signedIn) userRepository.getUser()
        return signedIn
    }

    override suspend fun signIn(email: String, password: String): DataState<Unit> {
        ensureClerkReady()?.let { return DataState.Error(it) }
        val submittedEmail = email
        val submittedPassword = password
        return when (val result = Clerk.auth.signInWithPassword {
            identifier = submittedEmail
            this.password = submittedPassword
        }) {
            is ClerkResult.Success -> {
                Log.d(TAG, "signIn success: id=${result.value.id}, status=${result.value.status}, createdSessionId=${result.value.createdSessionId}")
                completeSignIn(result.value, userFromEmail(email))
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "signIn failure: ${result.authErrorMessage}; detail=${result.debugDescription}", result.throwable)
                DataState.Error(result.authErrorMessage)
            }
        }
    }

    override suspend fun continueWithGoogle(): DataState<Unit> {
        ensureClerkReady()?.let { return DataState.Error(it) }
        return when (val result = Clerk.auth.signUpWithGoogleOneTap()) {
            is ClerkResult.Success -> {
                Log.d(TAG, "google auth success: signIn=${result.value.signIn?.status}, signUp=${result.value.signUp?.status}")
                completeOAuthResult(
                    result = result.value,
                    user = User(displayName = "User", email = ""),
                )
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "google auth failure: ${result.authErrorMessage}; detail=${result.debugDescription}", result.throwable)
                DataState.Error(result.authErrorMessage)
            }
        }
    }

    override suspend fun signUp(email: String, password: String, username: String): DataState<SignUpOutcome> {
        ensureClerkReady()?.let { return DataState.Error(it) }
        val submittedEmail = email
        val submittedPassword = password
        val submittedUsername = username
        return when (val result = Clerk.auth.signUp {
            this.email = submittedEmail
            this.password = submittedPassword
            this.username = submittedUsername
        }) {
            is ClerkResult.Success -> {
                Log.d(TAG, "signUp success: id=${result.value.id}, status=${result.value.status}, unverified=${result.value.unverifiedFields}, missing=${result.value.missingFields}, createdSessionId=${result.value.createdSessionId}")
                handleSignUpResult(result.value, User(displayName = username, email = email))
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "signUp failure: ${result.authErrorMessage}", result.throwable)
                DataState.Error(result.authErrorMessage)
            }
        }
    }

    override suspend fun verifyEmail(signUpId: String, code: String): DataState<Unit> {
        ensureClerkReady()?.let { return DataState.Error(it) }
        val signUp = Clerk.auth.currentSignUp
            ?: return DataState.Error(getString(Res.string.error_verification_no_session))

        return when (val result = signUp.verifyCode(code, VerificationType.EMAIL)) {
            is ClerkResult.Success -> {
                Log.d(TAG, "verifyEmail success: id=${result.value.id}, status=${result.value.status}, createdUserId=${result.value.createdUserId}, createdSessionId=${result.value.createdSessionId}")
                completeSignUp(result.value, pendingUser)
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "verifyEmail failure: ${result.authErrorMessage}", result.throwable)
                DataState.Error(result.authErrorMessage)
            }
        }
    }

    override suspend fun signOut(): DataState<Unit> {
        if (ensureClerkReady() == null) {
            Clerk.auth.signOut()
        }
        tokenStorage.clearToken()
        userRepository.clearUser()
        pendingUser = null
        return DataState.Success(Unit)
    }

    private suspend fun completeOAuthResult(
        result: OAuthResult,
        user: User,
    ): DataState<Unit> {
        result.signIn?.let { return completeSignIn(it, user) }
        result.signUp?.let { return completeSignUp(it, user) }
        return DataState.Error(getString(Res.string.error_sign_in_no_session))
    }

    private suspend fun completeSignIn(signIn: SignIn, user: User?): DataState<Unit> {
        if (signIn.status != SignIn.Status.COMPLETE) {
            return DataState.Error(
                getString(Res.string.error_sign_in_status, signIn.status.name.lowercase())
            )
        }

        val sessionId = signIn.createdSessionId
            ?: return DataState.Error(getString(Res.string.error_sign_in_no_session))

        return when (val activeResult = Clerk.auth.setActive(sessionId)) {
            is ClerkResult.Success -> {
                Log.d(TAG, "setActive success: sessionId=$sessionId")
                saveCurrentToken(user)
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "setActive failure: ${activeResult.authErrorMessage}", activeResult.throwable)
                DataState.Error(activeResult.authErrorMessage)
            }
        }
    }

    private suspend fun handleSignUpResult(
        signUp: SignUp,
        user: User,
    ): DataState<SignUpOutcome> = when (signUp.status) {
        SignUp.Status.COMPLETE ->
            completeSignUp(signUp, user).mapSuccess { SignUpOutcome.Complete }
        SignUp.Status.MISSING_REQUIREMENTS -> {
            pendingUser = user
            sendEmailVerificationCode(signUp, user.email)
        }
        else -> DataState.Error(
            getString(Res.string.error_sign_up_status, signUp.status.name.lowercase())
        )
    }

    private suspend fun sendEmailVerificationCode(
        signUp: SignUp,
        email: String,
    ): DataState<SignUpOutcome> = when (val result = signUp.sendEmailCode()) {
        is ClerkResult.Success -> {
            Log.d(TAG, "sendEmailCode success: id=${result.value.id}, status=${result.value.status}, unverified=${result.value.unverifiedFields}")
            DataState.Success(SignUpOutcome.NeedsEmailVerification(result.value.id))
        }
        is ClerkResult.Failure -> {
            Log.e(TAG, "sendEmailCode failure: ${result.authErrorMessage}", result.throwable)
            DataState.Error(result.authErrorMessage)
        }
    }

    private suspend fun completeSignUp(signUp: SignUp, user: User?): DataState<Unit> {
        if (signUp.status != SignUp.Status.COMPLETE) {
            return DataState.Error(
                getString(Res.string.error_sign_up_status, signUp.status.name.lowercase())
            )
        }

        val sessionId = signUp.createdSessionId
            ?: return DataState.Error(getString(Res.string.error_sign_up_no_session))

        return when (val activeResult = Clerk.auth.setActive(sessionId)) {
            is ClerkResult.Success -> {
                Log.d(TAG, "setActive success: sessionId=$sessionId")
                saveCurrentToken(user)
            }
            is ClerkResult.Failure -> {
                Log.e(TAG, "setActive failure: ${activeResult.authErrorMessage}", activeResult.throwable)
                DataState.Error(activeResult.authErrorMessage)
            }
        }
    }

    private suspend fun saveCurrentToken(user: User?): DataState<Unit> = when (val tokenResult = Clerk.auth.getToken()) {
        is ClerkResult.Success -> {
            Log.d(TAG, "token fetch success")
            tokenStorage.saveToken(tokenResult.value)
            user?.let { userRepository.setUser(it) }
            pendingUser = null
            DataState.Success(Unit)
        }
        is ClerkResult.Failure -> DataState.Error(
            tokenResult.authErrorMessage.takeIf { it.isNotBlank() }
                ?: getString(Res.string.error_sign_in_no_session)
        )
    }

    private suspend fun ensureClerkReady(): String? {
        val (_, initializationError) = combine(
            Clerk.isInitialized,
            Clerk.initializationError,
        ) { isInitialized, error -> isInitialized to error }
            .first { (isInitialized, error) -> isInitialized || error != null }

        return initializationError?.message
    }

    private inline fun <T, R> DataState<T>.mapSuccess(transform: (T) -> R): DataState<R> =
        when (this) {
            is DataState.Success -> DataState.Success(transform(data))
            is DataState.Error -> this
            DataState.Loading -> DataState.Loading
        }
}

private val ClerkResult.Failure<ClerkErrorResponse>.authErrorMessage: String
    get() {
        val apiError = error?.errors?.firstOrNull()
        return apiError?.longMessage
            ?: apiError?.message
            ?: apiError?.code?.let { "Clerk error: $it" }
            ?: throwable?.message
            ?: code?.let { "Clerk request failed with HTTP $it" }
            ?: when (errorType) {
                ClerkResult.Failure.ErrorType.API ->
                    "Clerk rejected the auth request but returned no message. Confirm the account exists in Clerk, Native API is enabled, and email/password auth is enabled."
                ClerkResult.Failure.ErrorType.HTTP ->
                    "Clerk request failed without an error body. Check network access and Clerk configuration."
                ClerkResult.Failure.ErrorType.UNKNOWN ->
                    "Clerk auth failed before receiving a usable response. Check Logcat for GlimpseAuth details."
            }
    }

private val ClerkResult.Failure<ClerkErrorResponse>.debugDescription: String
    get() = buildString {
        append("type=").append(errorType)
        code?.let { append(", http=").append(it) }
        error?.clerkTraceId?.let { append(", trace=").append(it) }
        error?.errors?.firstOrNull()?.code?.let { append(", clerkCode=").append(it) }
        throwable?.let { append(", throwable=").append(it::class.simpleName).append(':').append(it.message) }
    }

private const val TAG = "GlimpseAuth"

private fun userFromEmail(email: String): User = User(
    displayName = email.substringBefore('@').ifBlank { "User" },
    email = email,
)
