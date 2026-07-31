package com.example.glimpse.core.data.storage

import com.example.glimpse.core.model.User

interface UserStorage {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
    suspend fun clearUser()
}
