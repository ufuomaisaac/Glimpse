package com.example.glimpse.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareLinkDto(
    val token: String,
    val url: String,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("cluster_id") val clusterId: String,
    @SerialName("expires_at") val expiresAt: String?,
    @SerialName("password_protected") val passwordProtected: Boolean = false,
)
