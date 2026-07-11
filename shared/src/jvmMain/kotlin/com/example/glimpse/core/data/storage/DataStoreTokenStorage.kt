package com.example.glimpse.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.glimpse.core.network.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

private val TOKEN_KEY = stringPreferencesKey("session_token")

class DataStoreTokenStorage : TokenStorage, TokenProvider {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        producePath = {
            val dir = System.getProperty("user.home") + "/.glimpsee"
            java.io.File(dir).mkdirs()
            "$dir/auth.preferences_pb".toPath()
        }
    )

    override suspend fun getToken(): String? =
        dataStore.data.map { it[TOKEN_KEY] }.firstOrNull()

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}
