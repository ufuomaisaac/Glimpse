package com.example.glimpse.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.glimpse.core.network.TokenProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private val TOKEN_KEY = stringPreferencesKey("session_token")

@OptIn(ExperimentalForeignApi::class)
private fun createDataStore(): DataStore<Preferences> {
    val docDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!.path!!
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { "$docDir/auth.preferences_pb".toPath() }
    )
}

class DataStoreTokenStorage : TokenStorage, TokenProvider {

    private val dataStore = createDataStore()

    override suspend fun getToken(): String? =
        dataStore.data.map { it[TOKEN_KEY] }.firstOrNull()

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}
