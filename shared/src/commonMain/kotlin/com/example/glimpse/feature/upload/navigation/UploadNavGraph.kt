package com.example.glimpse.feature.upload.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.glimpse.feature.upload.ui.CreateFirstEventRoute
import kotlinx.serialization.Serializable

@Serializable
object CreateFirstEventDestination

fun NavGraphBuilder.createFirstEventDestination(
    onCreateEvent: () -> Unit,
) {
    composable<CreateFirstEventDestination> {
        CreateFirstEventRoute(onCreateEvent = onCreateEvent)
    }
}
