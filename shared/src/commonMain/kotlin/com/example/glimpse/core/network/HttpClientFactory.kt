package com.example.glimpse.core.network

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultrequest.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal fun createHttpClient(tokenProvider: TokenProvider): HttpClient {
    val log = Logger.withTag("HttpClient")

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            level = LogLevel.BODY
            logger = object : KtorLogger {
                override fun log(message: String) = log.d { message }
            }
        }

        defaultRequest {
            url(ApiEndPoints.BASE_URL)
        }
    }

    // Attach bearer token on every request; gallery endpoints work fine
    // when getToken() returns null — no Authorization header is added.
    client.plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (token != null) {
            request.headers["Authorization"] = "Bearer $token"
        }
        execute(request)
    }

    return client
}
