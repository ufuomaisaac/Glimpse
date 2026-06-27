package com.example.glimpse.core.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.glimpse.core.network.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore("auth")

private val TOKEN_KEY = stringPreferencesKey("session_token")

class DataStoreTokenStorage(private val context: Context) : TokenStorage, TokenProvider {

    override suspend fun getToken(): String? =
        context.tokenDataStore.data.map { it[TOKEN_KEY] }.firstOrNull()

    override suspend fun saveToken(token: String) {
        context.tokenDataStore.edit { it[TOKEN_KEY] = token }
    }

    override suspend fun clearToken() {
        context.tokenDataStore.edit { it.remove(TOKEN_KEY) }
    }
}
