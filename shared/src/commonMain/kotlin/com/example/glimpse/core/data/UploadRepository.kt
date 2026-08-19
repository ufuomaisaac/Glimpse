package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState

interface UploadRepository {
    suspend fun createUpload(
        name: String,
        expiresAt: String,
        fileNames: List<String>,
    ): ScreenState<Unit>
}
