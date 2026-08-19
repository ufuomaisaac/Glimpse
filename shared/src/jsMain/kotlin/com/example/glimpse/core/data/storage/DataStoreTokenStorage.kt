package com.example.glimpse.core.data.storage

import com.example.glimpse.core.model.User
import com.example.glimpse.core.network.TokenProvider
import kotlinx.browser.localStorage

private const val TOKEN_KEY = "session_token"
private const val DISPLAY_NAME_KEY = "user_display_name"
private const val EMAIL_KEY = "user_email"

class DataStoreTokenStorage : TokenStorage, UserStorage, TokenProvider {
    override suspend fun getToken(): String? = localStorage.getItem(TOKEN_KEY)

    override suspend fun saveToken(token: String) {
        localStorage.setItem(TOKEN_KEY, token)
    }

    override suspend fun clearToken() {
        localStorage.removeItem(TOKEN_KEY)
    }

    override suspend fun getUser(): User? {
        val displayName = localStorage.getItem(DISPLAY_NAME_KEY) ?: return null
        val email = localStorage.getItem(EMAIL_KEY) ?: return null
        return User(displayName = displayName, email = email)
    }

    override suspend fun saveUser(user: User) {
        localStorage.setItem(DISPLAY_NAME_KEY, user.displayName)
        localStorage.setItem(EMAIL_KEY, user.email)
    }

    override suspend fun clearUser() {
        localStorage.removeItem(DISPLAY_NAME_KEY)
        localStorage.removeItem(EMAIL_KEY)
    }
}
