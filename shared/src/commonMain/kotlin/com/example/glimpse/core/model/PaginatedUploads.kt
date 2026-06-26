package com.example.glimpse.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedUploads(
    val data: List<Upload>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
)
