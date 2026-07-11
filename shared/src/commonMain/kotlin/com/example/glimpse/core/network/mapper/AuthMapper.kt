package com.example.glimpse.core.network.mapper

import com.example.glimpse.core.model.AuthSession
import com.example.glimpse.core.network.dto.auth.ClerkSignInResponseDto
import com.example.glimpse.core.network.dto.auth.ClerkSignUpResponseDto

fun ClerkSignInResponseDto.toDomain(): AuthSession? {
    val sessionId = response.createdSessionId ?: return null
    val token = client.sessions.find { it.id == sessionId }?.lastActiveToken?.jwt ?: return null
    return AuthSession(sessionId = sessionId, token = token)
}

fun ClerkSignUpResponseDto.toDomain(): AuthSession? {
    val sessionId = response.createdSessionId ?: return null
    val token = client.sessions.find { it.id == sessionId }?.lastActiveToken?.jwt ?: return null
    return AuthSession(sessionId = sessionId, token = token)
}
