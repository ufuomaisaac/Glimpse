package com.example.glimpse.core.network.mapper

import com.example.glimpse.core.model.Cluster
import com.example.glimpse.core.model.ShareLink
import com.example.glimpse.core.network.dto.ClusterDto
import com.example.glimpse.core.network.dto.ShareLinkDto

fun ClusterDto.toDomain() = Cluster(
    id = id,
    name = name,
    thumbnailUrl = thumbnailUrl,
    photoCount = photoCount,
    uploadSessionId = uploadSessionId,
    links = links.map { it.toDomain() },
)

fun ShareLinkDto.toDomain() = ShareLink(
    token = token,
    url = url,
    isActive = isActive,
    clusterId = clusterId,
    expiresAt = expiresAt,
    passwordProtected = passwordProtected,
)
