package com.example.glimpse.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedUploadsDto(
    val data: List<UploadDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
)
