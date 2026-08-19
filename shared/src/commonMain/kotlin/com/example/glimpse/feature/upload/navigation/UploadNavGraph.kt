package com.example.glimpse.feature.upload.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.glimpse.feature.upload.ui.CreateFirstEventRoute
import com.example.glimpse.feature.upload.model.SelectedPhoto
import com.example.glimpse.feature.upload.ui.CreateEventRoute
import kotlinx.serialization.Serializable

@Serializable
object CreateFirstEventDestination

@Serializable
object CreateEventDestination

fun NavGraphBuilder.createFirstEventDestination(
    onCreateEvent: () -> Unit,
) {
    composable<CreateFirstEventDestination> {
        CreateFirstEventRoute(onCreateEvent = onCreateEvent)
    }
}

fun NavGraphBuilder.createEventDestination(
    onBack: () -> Unit,
    onUploadRequest: (eventName: String, photos: List<SelectedPhoto>) -> Unit,
) {
    composable<CreateEventDestination> {
        CreateEventRoute(onBack = onBack, onUploadRequest = onUploadRequest)
    }
}
