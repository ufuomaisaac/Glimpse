package com.example.glimpse.core.data

import com.example.glimpse.core.data.storage.TokenStorage
import com.example.glimpse.core.network.service.AuthApiService

actual fun createPlatformAuthRepository(
    authApiService: AuthApiService,
    tokenStorage: TokenStorage,
    userRepository: UserRepository,
): AuthRepository = AuthRepositoryImpl(authApiService, tokenStorage, userRepository)
