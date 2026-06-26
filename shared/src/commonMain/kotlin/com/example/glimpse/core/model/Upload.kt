package com.example.glimpse.core.model

data class Upload(
    val id: String,
    val name: String,
    val hostId: String,
    val status: UploadStatus,
    val expiresAt: String?,
    val createdAt: String,
    val updatedAt: String,
)
