package com.example.glimpse.feature.upload.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glimpse.core.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CreateFirstEventUiState(
    val displayName: String = "",
)

class CreateFirstEventViewModel(
    userRepository: UserRepository,
) : ViewModel() {

    val uiState: StateFlow<CreateFirstEventUiState> = userRepository.currentUser
        .map { user ->
            CreateFirstEventUiState(
                displayName = user?.displayName.orEmpty(),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CreateFirstEventUiState(
                displayName = userRepository.currentUser.value?.displayName.orEmpty(),
            ),
        )
}
