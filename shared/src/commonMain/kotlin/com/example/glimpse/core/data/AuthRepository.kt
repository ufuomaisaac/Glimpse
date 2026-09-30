package com.example.glimpse.core.data

import com.example.glimpse.core.common.DataState
import com.example.glimpse.core.model.SignUpOutcome

interface AuthRepository {
    suspend fun isSignedIn(): Boolean
    suspend fun signIn(email: String, password: String): DataState<Unit>
    suspend fun continueWithGoogle(): DataState<Unit>
    suspend fun signUp(email: String, password: String, username: String): DataState<SignUpOutcome>
    suspend fun verifyEmail(signUpId: String, code: String): DataState<Unit>
    suspend fun signOut(): DataState<Unit>
}
