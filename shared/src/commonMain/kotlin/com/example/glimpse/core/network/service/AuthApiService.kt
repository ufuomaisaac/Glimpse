package com.example.glimpse.core.network.service

import com.example.glimpse.core.network.ApiEndPoints
import com.example.glimpse.core.network.ApiEndPoints.AUTH_URL
import com.example.glimpse.core.network.ApiEndPoints.SIGN_IN
import com.example.glimpse.core.network.ApiEndPoints.SIGN_UP
import com.example.glimpse.core.network.dto.auth.ClerkSignInResponseDto
import com.example.glimpse.core.network.dto.auth.ClerkSignUpResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class AuthApiService(private val client: HttpClient) {

    suspend fun signIn(email: String, password: String): ClerkSignInResponseDto =
        client.submitForm(
            url = SIGN_IN,
            formParameters = parameters {
                append("identifier", email)
                append("strategy", "password")
                append("password", password)
            }
        ).body()

    suspend fun signUp(email: String, password: String, username: String): ClerkSignUpResponseDto =
        client.submitForm(
            url = SIGN_UP,
            formParameters = parameters {
                append("email_address", email)
                append("password", password)
                append("username", username)
            }
        ).body()

    suspend fun prepareEmailVerification(signUpId: String): HttpResponse =
        client.post(ApiEndPoints.prepareEmailVerification(signUpId)) {
            contentType(ContentType.Application.Json)
            setBody(PrepareVerificationRequest("email_code"))
        }

    suspend fun verifyEmail(signUpId: String, code: String): ClerkSignUpResponseDto =
        client.post(ApiEndPoints.verifyEmail(signUpId)) {
            contentType(ContentType.Application.Json)
            setBody(VerifyEmailRequest(code))
        }.body()

    suspend fun signOut(sessionId: String): HttpResponse =
        client.delete(ApiEndPoints.signOut(sessionId))

    @Serializable
    private data class PrepareVerificationRequest(val strategy: String)

    @Serializable
    private data class VerifyEmailRequest(val code: String)
}
