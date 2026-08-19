package com.example.glimpse.core.data.storage

import com.example.glimpse.core.model.User
import com.example.glimpse.core.network.TokenProvider

// WasmJS targets the recipient gallery which has no auth requirement;
// in-memory storage is sufficient.
class DataStoreTokenStorage : TokenStorage, UserStorage, TokenProvider {
    private var token: String? = null
    private var user: User? = null

    override suspend fun getToken(): String? = token

    override suspend fun saveToken(token: String) {
        this.token = token
    }

    override suspend fun clearToken() {
        token = null
    }

    override suspend fun getUser(): User? = user

    override suspend fun saveUser(user: User) {
        this.user = user
    }

    override suspend fun clearUser() {
        user = null
    }
}
