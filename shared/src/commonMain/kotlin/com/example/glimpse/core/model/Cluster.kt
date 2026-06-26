package com.example.glimpse.core.model

data class Cluster(
    val id: String,
    val name: String?,
    val thumbnailUrl: String,
    val photoCount: Int,
    val uploadSessionId: String,
    val links: List<ShareLink> = emptyList(),
)
