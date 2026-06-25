package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.FaceCluster
import com.example.glimpse.core.model.ShareLink
import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ClusterApiService(private val client: HttpClient) {

    suspend fun completeUpload(sessionId: String): HttpResponse {
        return client.post(ApiEndPoints.uploadStatus(sessionId))
    }

    suspend fun getClusters(): List<FaceCluster> {
        return client.get(ApiEndPoints.GET_RECEIVED_CLUSTER).body<List<FaceCluster>>()
    }

    suspend fun createLink(clusterId: String): ShareLink {
        return client.post(ApiEndPoints.clusterLinks(clusterId)).body()
    }
}
