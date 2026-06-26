package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.Upload
import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

class UploadApiService(private val client: HttpClient) {

    fun getAllUploads(): Flow<List<Upload>> = flow {
        emit(client.get(ApiEndPoints.GET_ALL_UPLOADS).body())
    }

    fun getUploadById(id: String): Flow<Upload> = flow {
        emit(client.get(ApiEndPoints.getUploadById(id)).body())
    }

    suspend fun deleteUpload(id: String): HttpResponse =
        client.delete(ApiEndPoints.deleteUpload(id))

    suspend fun createUpload(name: String, expiresAt: String): HttpResponse {
        return client.post(ApiEndPoints.CREATE_UPLOAD) {
            contentType(ContentType.Application.Json)
            setBody(CreateUploadRequest(name, expiresAt))
        }
    }

    suspend fun updateUpload(uploadId: String, name: String, expiresAt: String): HttpResponse {
        return client.patch(ApiEndPoints.updateUpload(uploadId)) {
            contentType(ContentType.Application.Json)
            setBody(UpdateUploadRequest(name, expiresAt))
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
    private data class UpdateUploadRequest(val name: String, val expiresAt: String)
}
