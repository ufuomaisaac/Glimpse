package com.example.glimpse.core.network.service

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
            url = "/v1/client/sign_ins",
            formParameters = parameters {
                append("identifier", email)
                append("strategy", "password")
                append("password", password)
            }
        ).body()

    suspend fun signUp(email: String, password: String): ClerkSignUpResponseDto =
        client.submitForm(
            url = "/v1/client/sign_ups",
            formParameters = parameters {
                append("email_address", email)
                append("password", password)
            }
        ).body()

    suspend fun prepareEmailVerification(signUpId: String): HttpResponse =
        client.post("/v1/client/sign_ups/$signUpId/prepare_email_address_verification") {
            contentType(ContentType.Application.Json)
            setBody(PrepareVerificationRequest("email_code"))
        }

    suspend fun verifyEmail(signUpId: String, code: String): ClerkSignUpResponseDto =
        client.post("/v1/client/sign_ups/$signUpId/attempt_email_address_verification") {
            contentType(ContentType.Application.Json)
            setBody(VerifyEmailRequest(code))
        }.body()

    suspend fun signOut(sessionId: String): HttpResponse =
        client.delete("/v1/client/sessions/$sessionId")

    @Serializable
    private data class PrepareVerificationRequest(val strategy: String)

    @Serializable
    private data class VerifyEmailRequest(val code: String)
}
