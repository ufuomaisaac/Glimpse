package com.example.glimpse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.glimpse.feature.auth.navigation.AuthGraph
import com.example.glimpse.feature.auth.navigation.authGraph
import com.example.glimpse.feature.upload.navigation.CreateEventDestination
import com.example.glimpse.feature.upload.navigation.CreateFirstEventDestination
import com.example.glimpse.feature.upload.navigation.createEventDestination
import com.example.glimpse.feature.upload.navigation.createFirstEventDestination
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable object MainGraph

@Composable
fun AppNavHost(viewModel: AppNavViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val destination = startDestination ?: return
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = destination) {
        authGraph(
            navController = navController,
            onNavigateToMain = {
                navController.navigate(MainGraph) {
                    popUpTo(AuthGraph) { inclusive = true }
                }
            },
            onFirstAuthentication = {
                navController.navigate(CreateFirstEventDestination) {
                    popUpTo(AuthGraph) { inclusive = true }
                }
            },
        )
        createFirstEventDestination(
            onCreateEvent = {
                navController.navigate(CreateEventDestination)
            },
        )
        createEventDestination(
            onBack = { navController.popBackStack() },
        )

        navigation<MainGraph>(startDestination = CreateEventDestination) {
            createEventDestination(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
