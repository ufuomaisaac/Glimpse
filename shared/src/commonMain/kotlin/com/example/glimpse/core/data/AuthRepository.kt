package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.model.SignUpOutcome

interface AuthRepository {
    suspend fun isSignedIn(): Boolean
    suspend fun signIn(email: String, password: String): ScreenState<Unit>
    suspend fun signUp(email: String, password: String, username: String): ScreenState<SignUpOutcome>
    suspend fun verifyEmail(signUpId: String, code: String): ScreenState<Unit>
    suspend fun signOut(): ScreenState<Unit>
}
