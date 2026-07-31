package com.example.glimpse.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.glimpse.core.model.User
import com.example.glimpse.core.network.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

private val tokenKey = stringPreferencesKey("session_token")
private val displayNameKey = stringPreferencesKey("user_display_name")
private val emailKey = stringPreferencesKey("user_email")

class DataStoreTokenStorage : TokenStorage, UserStorage, TokenProvider {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val dir = System.getProperty("user.home") + "/.glimpsee"
            java.io.File(dir).mkdirs()
            "$dir/auth.preferences_pb".toPath()
        },
    )

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
