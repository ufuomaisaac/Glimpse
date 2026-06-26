package com.example.glimpse.core.model

enum class UploadStatus {
    PENDING, PROCESSING, DONE, FAILED;

    companion object {
        fun fromValue(value: String) = when (value) {
            "pending" -> PENDING
            "processing" -> PROCESSING
            "done" -> DONE
            "failed" -> FAILED
            else -> throw IllegalArgumentException("Unknown upload status: $value")
        }
    }
}
