package com.example.glimpse.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseIcons
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GlimpseIconTextRow(
    icon: DrawableResource,
    darkThemeIcon: DrawableResource,
    text: String,
    modifier: Modifier = Modifier,
    iconContentDescription: String? = null,
    iconSize: Dp = GlimpseDp.dp30,
    spacing: Dp = GlimpseDp.dp8,
    iconTint: Color = Color.Unspecified,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = GlimpseTextStyles.labelMedium,
) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val resolvedIcon = if (isDarkTheme) darkThemeIcon else icon

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(resolvedIcon),
            contentDescription = iconContentDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize),
        )

        Text(
            text = text,
            color = textColor,
            style = textStyle,
            fontSize = GlimpseSp.sp14
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GlimpseIconTextRowPreview() {
    GlimpseTheme {
        GlimpseIconTextRow(
            icon = GlimpseIcons.ShareLight,
            darkThemeIcon = GlimpseIcons.ShareDark,
            text = "Share glimpse",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GlimpseIconTextRowDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        GlimpseIconTextRow(
            icon = GlimpseIcons.ShareLight,
            darkThemeIcon = GlimpseIcons.ShareDark,
            text = "Share glimpse",
        )
    }
}
