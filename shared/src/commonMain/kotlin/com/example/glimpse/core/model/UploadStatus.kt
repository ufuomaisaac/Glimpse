package com.example.glimpse.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UploadStatus {
    @SerialName("pending") PENDING,
    @SerialName("processing") PROCESSING,
    @SerialName("complete") COMPLETE,
    @SerialName("failed") FAILED,
}
