package com.example.glimpse.core.network

interface TokenProvider {
    suspend fun getToken(): String?
}
