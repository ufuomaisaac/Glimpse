package com.example.glimpse

import androidx.compose.runtime.Composable
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.navigation.AppNavHost

@Composable
fun App() {
    GlimpseTheme {
        AppNavHost()
    }
}
