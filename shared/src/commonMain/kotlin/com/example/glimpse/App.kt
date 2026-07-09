package com.example.glimpse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.feature.auth.ui.GlimpseGradientScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.glimpse.feature.auth.navigation.AuthGraph
import com.example.glimpse.feature.auth.navigation.authGraph
import com.example.glimpse.navigation.AppViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable object MainGraph

@Composable
fun App(viewModel: AppViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    GlimpseTheme {
        val dest = startDestination
        if (dest == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = dest) {
                authGraph(
                    navController = navController,
                    onNavigateToMain = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }
                )
                composable<MainGraph> {
                    GlimpseGradientScreen()
                }
            }
        }
    }
}
