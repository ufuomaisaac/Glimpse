package com.example.glimpse.core.data

import co.touchlab.kermit.Logger
import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.network.service.UploadApiService
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_upload_failed
import glimpse.shared.generated.resources.error_upload_failed_with_status
import io.ktor.client.statement.bodyAsText
import org.jetbrains.compose.resources.getString

class UploadRepositoryImpl(
    private val uploadApiService: UploadApiService,
) : UploadRepository {

    private val logger = Logger.withTag("UploadApiService")

    override suspend fun upload(
        name: String,
        expiresAt: String,
        fileNames: List<String>,
    ): ScreenState<Unit> = try {
        val response = uploadApiService.upload(name, expiresAt, fileNames)
        val responseBody = response.bodyAsText()

        logger.i {
            "Upload response: status=${response.status.value}, body=$responseBody"
        }

        if (response.status.value in 200..299) {
            ScreenState.Success(Unit)
        } else {
            val message = responseBody.takeIf(String::isNotBlank)
                ?: getString(
                    Res.string.error_upload_failed_with_status,
                    response.status.value,
                )
            ScreenState.Error(message)
        }
    } catch (exception: Exception) {
        ScreenState.Error(
            exception.message ?: getString(Res.string.error_upload_failed),
        )
    }
}
