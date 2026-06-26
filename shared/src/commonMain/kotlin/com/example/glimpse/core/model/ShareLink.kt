package com.example.glimpse.core.model

data class ShareLink(
    val token: String,
    val url: String,
    val isActive: Boolean,
    val clusterId: String,
    val expiresAt: String?,
    val passwordProtected: Boolean = false,
)
