package com.example.glimpse.core.network.service

import com.example.glimpse.core.network.ApiEndPoints
import com.example.glimpse.core.network.dto.ClusterDto
import com.example.glimpse.core.network.dto.ShareLinkDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ClusterApiService(private val client: HttpClient) {

    suspend fun getClusters(): List<ClusterDto> =
        client.get(ApiEndPoints.GET_RECEIVED_CLUSTER).body()

    suspend fun getReceivedClusterById(id: String): ClusterDto =
        client.get(ApiEndPoints.getReceivedClusterById(id)).body()

    suspend fun getGeneratedCluster(): List<ClusterDto> =
        client.get(ApiEndPoints.GENERATED_CLUSTER).body()

    suspend fun createLink(clusterId: String): ShareLinkDto =
        client.post(ApiEndPoints.clusterLinks(clusterId)).body()

}
