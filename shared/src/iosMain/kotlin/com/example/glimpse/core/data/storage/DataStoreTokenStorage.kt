package com.example.glimpse.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.glimpse.core.model.User
import com.example.glimpse.core.network.TokenProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private val tokenKey = stringPreferencesKey("session_token")
private val displayNameKey = stringPreferencesKey("user_display_name")
private val emailKey = stringPreferencesKey("user_email")

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
        produceFile = { "$docDir/auth.preferences_pb".toPath() },
    )
}

class DataStoreTokenStorage : TokenStorage, UserStorage, TokenProvider {

    private val dataStore = createDataStore()

    override suspend fun getToken(): String? =
        dataStore.data.map { it[tokenKey] }.firstOrNull()

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[tokenKey] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(tokenKey) }
    }

    override suspend fun getUser(): User? =
        dataStore.data.map { preferences ->
            val displayName = preferences[displayNameKey] ?: return@map null
            val email = preferences[emailKey] ?: return@map null
            User(displayName = displayName, email = email)
        }.firstOrNull()

    override suspend fun saveUser(user: User) {
        dataStore.edit {
            it[displayNameKey] = user.displayName
            it[emailKey] = user.email
        }
    }

    override suspend fun clearUser() {
        dataStore.edit {
            it.remove(displayNameKey)
            it.remove(emailKey)
        }
    }
}
