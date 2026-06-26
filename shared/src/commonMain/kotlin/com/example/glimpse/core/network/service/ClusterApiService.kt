package com.example.glimpse.core.network.service

import com.example.glimpse.core.model.FaceCluster
import com.example.glimpse.core.model.ShareLink
import com.example.glimpse.core.network.ApiEndPoints
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClusterApiService(private val client: HttpClient) {

    fun getReceivedClusterById(id: String): Flow<FaceCluster> = flow {
        emit(client.get(ApiEndPoints.getReceivedClusterById(id)).body())
    }

    fun getGeneratedCluster(): Flow<List<FaceCluster>> = flow {
        emit(client.get(ApiEndPoints.GENERATED_CLUSTER).body())
    }

    suspend fun getClusters(): List<FaceCluster> {
        return client.get(ApiEndPoints.GET_RECEIVED_CLUSTER).body<List<FaceCluster>>()
    }

    suspend fun createLink(clusterId: String): ShareLink {
        return client.post(ApiEndPoints.clusterLinks(clusterId)).body()
    }

}
