package com.example.glimpse.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.AuthRepository
import com.example.glimpse.core.model.SignUpOutcome
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.error_unexpected
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object SignedIn : AuthUiState
    data class AwaitingEmailVerification(val signUpId: String, val email: String) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun checkAuthState() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = if (authRepository.isSignedIn()) AuthUiState.SignedIn
                             else AuthUiState.Idle
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (val result = authRepository.signIn(email, password)) {
                is ScreenState.Success -> AuthUiState.SignedIn
                is ScreenState.Error -> AuthUiState.Error(result.message)
                else -> AuthUiState.Error(getString(Res.string.error_unexpected))
            }
        }
    }

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (val result = authRepository.signUp(email, password, username)) {
                is ScreenState.Success -> when (result.data) {
                    is SignUpOutcome.Complete -> AuthUiState.SignedIn
                    is SignUpOutcome.NeedsEmailVerification ->
                        AuthUiState.AwaitingEmailVerification(result.data.signUpId, email)
                }
                is ScreenState.Error -> AuthUiState.Error(result.message)
                else -> AuthUiState.Error(getString(Res.string.error_unexpected))
            }
        }
    }

    fun verifyEmail(signUpId: String, code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (val result = authRepository.verifyEmail(signUpId, code)) {
                is ScreenState.Success -> AuthUiState.SignedIn
                is ScreenState.Error -> AuthUiState.Error(result.message)
                else -> AuthUiState.Error(getString(Res.string.error_unexpected))
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}
