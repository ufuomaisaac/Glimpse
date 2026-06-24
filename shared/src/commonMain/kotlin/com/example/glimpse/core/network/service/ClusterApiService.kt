package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.FaceCluster
import com.example.glimpse.core.model.ShareLink
import com.example.glimpse.core.model.UploadSession
import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class ClusterApiService(private val client: HttpClient) {

    suspend fun completeUpload(sessionId: String): UploadSession {
        return client.post(ApiEndPoints.uploadStatus(sessionId)).body()
    }

    suspend fun getClusters(): List<FaceCluster> {
        return client.get(ApiEndPoints.GET_RECEIVED_CLUSTER).body()
    }

    suspend fun renameCluster(clusterId: String, name: String): FaceCluster {
        return client.patch(ApiEndPoints.getReceivedClusterById(clusterId)) {
            contentType(ContentType.Application.Json)
            setBody(RenameClusterRequest(name))
        }.body()
    }

    suspend fun createLink(clusterId: String): ShareLink {
        return client.post(ApiEndPoints.clusterLinks(clusterId)).body()
    }

    @Serializable
    private data class RenameClusterRequest(val name: String)
}
