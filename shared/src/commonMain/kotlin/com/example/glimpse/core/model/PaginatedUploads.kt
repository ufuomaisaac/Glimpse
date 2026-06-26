package com.example.glimpse.core.model

data class PaginatedUploads(
    val data: List<Upload>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
)
