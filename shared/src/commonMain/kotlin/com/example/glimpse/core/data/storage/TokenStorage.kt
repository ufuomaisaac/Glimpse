package com.example.glimpse.core.data.storage

interface TokenStorage {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}
