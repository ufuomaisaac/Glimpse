package com.example.glimpse.core.data

import com.example.glimpse.core.common.DataState

interface UploadRepository {
    suspend fun upload(
        name: String,
        expiresAt: String,
        fileNames: List<String>,
    ): DataState<Unit>
}
