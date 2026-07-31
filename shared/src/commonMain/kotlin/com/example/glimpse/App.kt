package com.example.glimpse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.glimpse.designsystem.Background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.feature.auth.navigation.AuthGraph
import com.example.glimpse.feature.auth.navigation.authGraph
import com.example.glimpse.feature.upload.navigation.CreateFirstEventDestination
import com.example.glimpse.feature.upload.navigation.createFirstEventDestination
import com.example.glimpse.navigation.AppViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable object MainGraph

@Composable
fun App(viewModel: AppViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    GlimpseTheme {
        val dest = startDestination
        if (dest != null) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = dest) {
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
                        // Connect the event details route here.
                    },
                )
                composable<MainGraph> {
                    StartupBackground()
                }
            }
        }
    }
}


@Composable
private fun StartupBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    )
}
