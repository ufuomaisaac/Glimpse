package com.example.glimpse.feature.upload.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glimpse.core.common.ScreenState
import com.example.glimpse.core.data.UploadRepository
import com.example.glimpse.feature.upload.model.SelectedPhoto
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

data class CreateEventUiState(
    val eventName: String = "",
    val photos: List<SelectedPhoto> = emptyList(),
    val expirationDays: Int = 7,
    val isUploading: Boolean = false,
    val errorMessage: String? = null,
) {
    val canUpload: Boolean
        get() = eventName.isNotBlank() && photos.isNotEmpty() && !isUploading

    val totalBytes: Long
        get() = photos.sumOf(SelectedPhoto::sizeBytes)
}

class CreateEventViewModel(
    private val uploadRepository: UploadRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun updateEventName(value: String) {
        _uiState.update { it.copy(eventName = value.take(MAX_EVENT_NAME_LENGTH)) }
    }

    fun updateExpirationDays(days: Int) {
        _uiState.update { it.copy(expirationDays = days) }
    }

    fun addPhotos(photos: List<SelectedPhoto>) {
        _uiState.update { state ->
            val existingIds = state.photos.mapTo(mutableSetOf(), SelectedPhoto::id)
            val newPhotos = photos.filter { existingIds.add(it.id) }
            state.copy(
                photos = (state.photos + newPhotos).take(MAX_PHOTOS),
                errorMessage = if (state.photos.size + newPhotos.size > MAX_PHOTOS) {
                    "You can add up to $MAX_PHOTOS photos per event"
                } else {
                    null
                },
            )
        }
    }

    fun removePhoto(id: String) {
        _uiState.update { state -> state.copy(photos = state.photos.filterNot { it.id == id }) }
    }

    fun upload() {
        val state = _uiState.value
        if (!state.canUpload) return

        _uiState.update { it.copy(isUploading = true, errorMessage = null) }
        viewModelScope.launch {
            val expiresAt = (Clock.System.now() + state.expirationDays.days).toString()
            when (
                val result = uploadRepository.upload(
                    name = state.eventName.trim(),
                    expiresAt = expiresAt,
                    fileNames = state.photos.map(SelectedPhoto::name),
                )
            ) {
                is ScreenState.Success -> {
                    _uiState.update { it.copy(isUploading = false) }
                }

                is ScreenState.Error -> {
                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            errorMessage = result.message,
                        )
                    }
                }

                ScreenState.Loading -> Unit
            }
        }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

private const val MAX_PHOTOS = 100
private const val MAX_EVENT_NAME_LENGTH = 80
