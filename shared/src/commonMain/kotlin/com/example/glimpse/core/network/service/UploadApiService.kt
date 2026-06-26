package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.Upload
import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
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

    suspend fun getPresignedUrls(uploadId: String, fileNames: List<String>): HttpResponse =
        client.post(ApiEndPoints.getPresignedUrls(uploadId)) {
            contentType(ContentType.Application.Json)
            setBody(GetPresignedUrlsRequest(fileNames.map { FileRequest(it) }))
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
    private data class CreateUploadRequest(val name: String, val expiresAt: String)

    @Serializable
    private data class UpdateUploadRequest(val name: String, val expiresAt: String)

    @Serializable
    private data class GetPresignedUrlsRequest(val files: List<FileRequest>)

    @Serializable
    private data class FileRequest(val name: String)
}
