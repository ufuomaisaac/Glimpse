package com.example.glimpse.core.network

import co.touchlab.kermit.Logger as KermitLogger
import com.example.glimpse.core.network.dto.auth.ClerkErrorResponseDto
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.BodyProgress
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.HttpHeaders
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal fun createAuthHttpClient(): HttpClient {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    return HttpClient {
        expectSuccess = false

        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.DEFAULT
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value >= 400) {
                    val body = response.bodyAsText()
                    val message = parseClerkErrorMessage(json, body)
                        ?: "Clerk request failed (${response.status.value})"
                    throw ClerkApiException(message)
                }
            }
        }
        defaultRequest {
            url(ClerkConfig.FRONTEND_API_URL)
            headers {
                append("Authorization", "Bearer ${ClerkConfig.PUBLISHABLE_KEY}")
            }
        }
    }
}

private fun parseClerkErrorMessage(json: Json, body: String): String? {
    val clerkError = runCatching {
        json.decodeFromString<ClerkErrorResponseDto>(body)
    }.getOrNull()

    return clerkError?.errors
        ?.firstNotNullOfOrNull { error -> error.longMessage ?: error.message }
        ?: body.takeIf { it.isNotBlank() }?.take(300)
}

internal fun createHttpClient(tokenProvider: TokenProvider): HttpClient {
    val authLogger = KermitLogger.withTag("HttpAuth")
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.DEFAULT
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
        defaultRequest {
            url(ApiEndPoints.BASE_URL)
        }

        install(BodyProgress)
    }

    client.plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (!token.isNullOrBlank()) {
            request.headers["Authorization"] = "Bearer $token"
            authLogger.d { "Attached bearer token to ${request.url}" }
            authLogger.d { "Raw bearer token: $token" }
        } else {
            authLogger.w { "No bearer token available for ${request.url}" }
        }
        execute(request)
    }

    return client
}
