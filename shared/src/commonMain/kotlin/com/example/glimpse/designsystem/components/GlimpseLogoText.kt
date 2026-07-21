package com.example.glimpse.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseIcons
import com.example.glimpse.designsystem.GlimpseTheme
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.app_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GlimpseLogoText(modifier: Modifier = Modifier) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val logo = if (isDarkTheme) GlimpseIcons.LogoMarkDark else GlimpseIcons.LogoMarkLight

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(GlimpseDp.dp8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(logo),
            contentDescription = null,
            modifier = Modifier.size(GlimpseDp.dp32),
        )
        Text(
            text = stringResource(Res.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GlimpseLogoTextPreview() {
    GlimpseTheme {
        GlimpseLogoText()
    }
}

@Preview(showBackground = true)
@Composable
private fun GlimpseLogoTextDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        GlimpseLogoText()
    }
}
