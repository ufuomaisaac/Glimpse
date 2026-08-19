package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.UploadStatus
import com.example.glimpse.core.network.ApiEndPoints
import com.example.glimpse.core.network.dto.PaginatedUploadsDto
import com.example.glimpse.core.network.dto.UploadDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class UploadApiService(private val client: HttpClient) {

    suspend fun getAllUploads(
        page: Int? = null,
        limit: Int? = null,
        sort: String? = null,
        order: String? = null,
        search: String? = null,
        status: UploadStatus? = null,
    ): PaginatedUploadsDto = client.get(ApiEndPoints.GET_ALL_UPLOADS) {
        page?.let { parameter("page", it) }
        limit?.let { parameter("limit", it) }
        sort?.let { parameter("sort", it) }
        order?.let { parameter("order", it) }
        search?.let { parameter("search", it) }
        status?.let { parameter("status", it.name.lowercase()) }
    }.body()

    suspend fun getUploadById(id: String): UploadDto =
        client.get(ApiEndPoints.getUploadById(id)).body()

    suspend fun deleteUpload(id: String): HttpResponse =
        client.delete(ApiEndPoints.deleteUpload(id))

    suspend fun createUpload(
        name: String,
        expiresAt: String,
        fileNames: List<String>,
    ): HttpResponse =
        client.post(ApiEndPoints.CREATE_UPLOAD_WITH_FILES) {
            contentType(ContentType.Application.Json)
            setBody(CreateUploadRequest(name, expiresAt, fileNames.map(::FileRequest)))
        }

    suspend fun updateUpload(uploadId: String, name: String, expiresAt: String): HttpResponse {
        return client.patch(ApiEndPoints.updateUpload(uploadId)) {
            contentType(ContentType.Application.Json)
            setBody(UpdateUploadRequest(name, expiresAt))
        }
    }

    suspend fun completeUpload(uploadId: String, keys: List<String>): HttpResponse =
        client.post(ApiEndPoints.uploadStatus(uploadId)) {
            contentType(ContentType.Application.Json)
            setBody(CompleteUploadRequest(keys.map { FileKeyRequest(it) }))
        }


    suspend fun uploadPhotos(
        presignedUrls: List<String>,
        photos: List<ByteArray>,
        onProgress: (Float) -> Unit,
    ) {
        photos.forEachIndexed { index, bytes ->
            client.put(presignedUrls[index]) {
                contentType(ContentType.Image.JPEG)
                setBody(bytes)
                onUpload { bytesSent, totalBytes ->
                    if (totalBytes != null && totalBytes > 0L) {
                        val overall = (index + bytesSent.toFloat() / totalBytes) / photos.size
                        onProgress(overall)
                    }
                }
            }
        }
    }

    @Serializable
    private data class CreateUploadRequest(
        val name: String,
        val expiresAt: String,
        val files: List<FileRequest>,
    )

    @Serializable
    private data class UpdateUploadRequest(val name: String, val expiresAt: String)

    @Serializable
    private data class CompleteUploadRequest(val files: List<FileKeyRequest>)

    @Serializable
    private data class FileKeyRequest(val key: String)

    @Serializable
    private data class FileRequest(val name: String)
}
