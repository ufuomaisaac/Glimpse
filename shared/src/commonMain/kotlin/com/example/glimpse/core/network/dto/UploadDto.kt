package com.example.glimpse.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadDto(
    val id: String,
    val name: String,
    val hostId: String,
    val status: String,
    val expiresAt: String?,
    val createdAt: String,
    val updatedAt: String,
)
