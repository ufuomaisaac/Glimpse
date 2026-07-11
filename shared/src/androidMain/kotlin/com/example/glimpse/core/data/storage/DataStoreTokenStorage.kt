package com.example.glimpse.core.data.storage

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.glimpse.core.network.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class DataStoreTokenStorage(private val context: Context) : TokenStorage, TokenProvider {

    private val dataStore = PreferenceDataStoreFactory.createWithPath(
        produceFile = { context.filesDir.resolve("auth.preferences_pb").absolutePath.toPath() }
    )

    private val TOKEN_KEY = stringPreferencesKey("session_token")

    override suspend fun getToken(): String? =
        dataStore.data.map { it[TOKEN_KEY] }.firstOrNull()

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}
