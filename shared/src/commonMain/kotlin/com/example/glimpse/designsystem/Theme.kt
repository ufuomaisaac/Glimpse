package com.example.glimpse.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface as MaterialSurface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GlimpseLightColorScheme = lightColorScheme(
    primary = AccentPrimary,
    onPrimary = Surface,
    primaryContainer = AccentTint,
    onPrimaryContainer = AccentTextOnTint,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = BorderLight,
)

private val GlimpseDarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = DarkSurface,
    primaryContainer = DarkAccentTint,
    onPrimaryContainer = DarkAccentTextOnTint,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkTextMuted,
    outlineVariant = DarkBorder,
)

@Composable
fun GlimpseTheme(
    darkTheme: Boolean = false,
    typography: Typography = glimpseTypography(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) GlimpseDarkColorScheme else GlimpseLightColorScheme,
        typography = typography,
    ) {
        MaterialSurface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
