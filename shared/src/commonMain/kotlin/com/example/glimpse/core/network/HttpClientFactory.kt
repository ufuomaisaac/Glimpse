package com.example.glimpse.core.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.BodyProgress
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal fun createAuthHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(Logging) {
        level = LogLevel.BODY
        logger = Logger.DEFAULT
    }
    defaultRequest {
        url(ClerkConfig.FRONTEND_API_URL)
        headers {
            append("Authorization", "Bearer ${ClerkConfig.PUBLISHABLE_KEY}")
        }
    }
}

internal fun createHttpClient(tokenProvider: TokenProvider): HttpClient {
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
        }
        defaultRequest {
            url(ApiEndPoints.BASE_URL)
        }

        install(BodyProgress)
    }

    client.plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (token != null) {
            request.headers["Authorization"] = "Bearer $token"
        }
        execute(request)
    }

    return client
}
