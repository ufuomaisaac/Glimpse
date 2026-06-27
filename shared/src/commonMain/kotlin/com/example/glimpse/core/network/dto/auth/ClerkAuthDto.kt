package com.example.glimpse.core.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClerkSessionTokenDto(val jwt: String)

@Serializable
data class ClerkSessionDto(
    val id: String,
    val status: String,
    @SerialName("last_active_token") val lastActiveToken: ClerkSessionTokenDto?,
)

@Serializable
data class ClerkClientDto(
    @SerialName("last_active_session_id") val lastActiveSessionId: String?,
    val sessions: List<ClerkSessionDto> = emptyList(),
)

@Serializable
data class ClerkSignInAttemptDto(
    val id: String,
    val status: String,
    @SerialName("created_session_id") val createdSessionId: String?,
)

@Serializable
data class ClerkSignInResponseDto(
    val response: ClerkSignInAttemptDto,
    val client: ClerkClientDto,
)

@Serializable
data class ClerkSignUpAttemptDto(
    val id: String,
    val status: String,
    @SerialName("created_session_id") val createdSessionId: String?,
    @SerialName("unverified_fields") val unverifiedFields: List<String> = emptyList(),
)

@Serializable
data class ClerkSignUpResponseDto(
    val response: ClerkSignUpAttemptDto,
    val client: ClerkClientDto,
)
