package com.example.glimpse.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UploadSession(
    val id: String,
    val name: String,
    val hostId: String,
    val status: UploadStatus,
    val expiresAt: String?,
    val createdAt: String,
    val updatedAt: String,
)
