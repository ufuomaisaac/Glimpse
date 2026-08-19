package com.example.glimpse.core.data

import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.network.service.UploadApiService
import io.ktor.client.statement.bodyAsText

class UploadRepositoryImpl(
    private val uploadApiService: UploadApiService,
) : UploadRepository {

    override suspend fun createUpload(
        name: String,
        expiresAt: String,
        fileNames: List<String>,
    ): ScreenState<Unit> = try {
        val response = uploadApiService.createUpload(name, expiresAt, fileNames)

        if (response.status.value in 200..299) {
            ScreenState.Success(Unit)
        } else {
            val message = response.bodyAsText().takeIf(String::isNotBlank)
                ?: "Could not create upload (${response.status.value})"
            ScreenState.Error(message)
        }
    } catch (exception: Exception) {
        ScreenState.Error(exception.message ?: "Could not create upload")
    }
}
