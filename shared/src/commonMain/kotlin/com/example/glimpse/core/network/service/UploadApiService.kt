package com.example.glimpse.core.network.service

import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class UploadApiService(private val client: HttpClient) {

    suspend fun createUpload(name: String, expiresAt: String): HttpResponse {
        return client.post(ApiEndPoints.CREATE_UPLOAD) {
            contentType(ContentType.Application.Json)
            setBody(CreateUploadRequest(name, expiresAt))
        }
    }

    suspend fun renameUpload(uploadId: String, name: String): HttpResponse {
        return client.patch(ApiEndPoints.updateUpload(uploadId)) {
            contentType(ContentType.Application.Json)
            setBody(RenameUploadRequest(name))
        }
    }

    suspend fun uploadPhotos(
        sessionId: String,
        photos: List<ByteArray>,
        onProgress: (Float) -> Unit,
    ): HttpResponse {
        return client.submitFormWithBinaryData(
            url = ApiEndPoints.getPresignedUrls(sessionId),
            formData = formData {
                photos.forEachIndexed { index, bytes ->
                    append(
                        key = "photos[$index]",
                        value = bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"photo_$index.jpg\"",
                            )
                        },
                    )
                }
            },
        ) {
            onUpload { bytesSent, totalBytes ->
                if (totalBytes != null && totalBytes > 0L) {
                    onProgress(bytesSent.toFloat() / totalBytes.toFloat())
                }
            }
        }
    }

    @Serializable
    private data class CreateUploadRequest(
        val name: String,
        val expiresAt: String,
    )

    @Serializable
    private data class RenameUploadRequest(val name: String)
}
