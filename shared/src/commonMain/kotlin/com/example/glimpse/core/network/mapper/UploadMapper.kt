package com.example.glimpse.core.network.mapper

import com.example.glimpse.core.model.PaginatedUploads
import com.example.glimpse.core.model.Upload
import com.example.glimpse.core.model.UploadStatus
import com.example.glimpse.core.network.dto.PaginatedUploadsDto
import com.example.glimpse.core.network.dto.UploadDto

fun UploadDto.toDomain() = Upload(
    id = id,
    name = name,
    hostId = hostId,
    status = UploadStatus.fromValue(status),
    expiresAt = expiresAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PaginatedUploadsDto.toDomain() = PaginatedUploads(
    data = data.map { it.toDomain() },
    total = total,
    page = page,
    limit = limit,
    totalPages = totalPages,
)
