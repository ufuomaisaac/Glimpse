package com.example.glimpse.core.data.storage

import com.example.glimpse.core.network.TokenProvider
import kotlinx.browser.localStorage

private const val TOKEN_KEY = "session_token"

class DataStoreTokenStorage : TokenStorage, TokenProvider {
    override suspend fun getToken(): String? = localStorage.getItem(TOKEN_KEY)
    override suspend fun saveToken(token: String) { localStorage.setItem(TOKEN_KEY, token) }
    override suspend fun clearToken() { localStorage.removeItem(TOKEN_KEY) }
}
