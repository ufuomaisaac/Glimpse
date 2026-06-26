package com.example.glimpse.core.network

internal object ApiEndPoints {
    const val BASE_URL = "http://localhost:8080/api/v1/"

    const val GET_ALL_UPLOADS            = "/uploads"
    const val CREATE_UPLOAD              = "/uploads"
    const val GET_RECEIVED_CLUSTER       = "/clusters"
    const val GENERATED_CLUSTER          = "/generated_cluster"

    fun getUploadById(id: String)        = "/uploads/$id"
    fun uploadStatus(id: String)         = "/uploads/$id/complete"
    fun updateUpload(id: String)         = "/uploads/$id"
    fun deleteUpload(id: String)         = "/uploads/$id"
    fun getPresignedUrls(id: String)     = "/uploads/$id/photos"
    fun getReceivedClusterById(id: String) = "/clusters/$id"
    fun clusterLinks(clusterId: String)  = "/clusters/$clusterId/links"
}
