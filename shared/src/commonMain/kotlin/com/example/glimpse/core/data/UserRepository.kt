package com.example.glimpse.core.data

import com.example.glimpse.core.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User?>

    suspend fun restore()
    suspend fun setUser(user: User)
    suspend fun clearUser()
}
