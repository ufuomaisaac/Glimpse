package com.example.glimpse.core.network.service

import co.touchlab.kermit.Logger
import com.example.glimpse.core.network.ApiEndPoints
import com.example.glimpse.core.network.dto.ClusterDto
import com.example.glimpse.core.network.dto.ShareLinkDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ClusterApiService(private val client: HttpClient) {

    private val logger = Logger.withTag("ClusterApiService")

    suspend fun getClusters(): List<ClusterDto> =
        client.get(ApiEndPoints.GET_RECEIVED_CLUSTER).body()

    suspend fun getGeneratedCluster(): List<ClusterDto> =
        client.get(ApiEndPoints.GENERATED_CLUSTER).body()

}
