package com.example.glimpse.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cluster(
    val id: String,
    val name: String?,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("photo_count") val photoCount: Int,
    @SerialName("upload_session_id") val uploadSessionId: String,
    val links: List<ShareLink> = emptyList(),
)
