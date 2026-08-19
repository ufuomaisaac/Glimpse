package com.example.glimpse.feature.upload.viewmodel

import androidx.lifecycle.ViewModel
import com.example.glimpse.feature.upload.model.SelectedPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateEventUiState(
    val eventName: String = "",
    val photos: List<SelectedPhoto> = emptyList(),
    val expirationDays: Int = 7,
    val errorMessage: String? = null,
) {
    val canUpload: Boolean
        get() = eventName.isNotBlank() && photos.isNotEmpty()

    val totalBytes: Long
        get() = photos.sumOf(SelectedPhoto::sizeBytes)
}

class CreateEventViewModel : ViewModel() {
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

    fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

private const val MAX_PHOTOS = 100
private const val MAX_EVENT_NAME_LENGTH = 80
