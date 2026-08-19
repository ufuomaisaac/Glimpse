package com.example.glimpse.core.data

import com.example.glimpse.core.data.storage.UserStorage
import com.example.glimpse.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepositoryImpl(
    private val storage: UserStorage,
) : UserRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun restore() {
        _currentUser.value = storage.getUser()
    }

    override suspend fun setUser(user: User) {
        storage.saveUser(user)
        _currentUser.value = user
    }

    override suspend fun clearUser() {
        storage.clearUser()
        _currentUser.value = null
    }
}
