package com.example.glimpse.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseIcons
import com.example.glimpse.designsystem.GlimpseTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.interFontFamily
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.auth_google
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GlimpsePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(GlimpseDp.dp28)
    val colors = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            disabledContainerColor = colors.primary.copy(alpha = 0.5f),
            disabledContentColor = colors.onPrimary.copy(alpha = 0.7f),
        ),
        contentPadding = PaddingValues(horizontal = GlimpseDp.dp28, vertical = GlimpseDp.dp16),
        modifier = modifier
            .shadow(
                elevation = GlimpseDp.dp8,
                shape = shape,
                spotColor = colors.primary.copy(alpha = 0.25f),
                ambientColor = colors.primary.copy(alpha = 0.10f),
            )
            .fillMaxWidth()
            .height(GlimpseDp.dp54),
    ) {
            Text(
                text = text,
                style = GlimpseTextStyles.headingH1,
                fontSize = GlimpseSp.sp16
            )
    }
}

@Composable
fun GlimpseSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = MaterialTheme.colorScheme
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(GlimpseDp.dp28),
        border = BorderStroke(GlimpseDp.dp2, colors.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(GlimpseDp.dp56),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun GlimpseGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(GlimpseDp.dp28)
    val isDarkTheme = colors.background.luminance() < 0.5f
    val containerColor = if (isDarkTheme) colors.surface else colors.background
    val topBorderColor = Color(0x260F172A)
    val shadowColor = Color(0x59000000)

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = null,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = colors.onSurface,
            disabledContainerColor = containerColor,
            disabledContentColor = colors.onSurface.copy(alpha = 0.38f),
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(GlimpseDp.dp56)
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = GlimpseDp.dp3,
                    color = shadowColor,
                    offset = DpOffset(x = GlimpseDp.dp0, y = GlimpseDp.dp1),
                ),
            )
            .clip(shape)
            .drawWithContent {
                drawContent()
                val strokeWidth = GlimpseDp.dp1.toPx()
                drawLine(
                    color = topBorderColor,
                    start = Offset(0f, strokeWidth / 2f),
                    end = Offset(size.width, strokeWidth / 2f),
                    strokeWidth = strokeWidth,
                )
            },
    ) {
        Image(
            painter = painterResource(GlimpseIcons.GoogleDark),
            contentDescription = null,
            modifier = Modifier.size(GlimpseDp.dp20),
        )
        Spacer(Modifier.width(GlimpseDp.dp8))
        Text(
            text = stringResource(Res.string.auth_google),
            style = TextStyle(
                fontFamily = interFontFamily(),
                fontWeight = FontWeight.W600,
                fontSize = GlimpseSp.sp16,
                lineHeight = GlimpseSp.sp20,

            )
        )
    }
}

@Composable
fun GlimpseAccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(GlimpseDp.dp24),
            verticalArrangement = Arrangement.spacedBy(GlimpseDp.dp16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpsePrimaryButton(text = "Sign in", onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(GlimpseDp.dp24),
            verticalArrangement = Arrangement.spacedBy(GlimpseDp.dp16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpseSecondaryButton(text = "Continue with Google", onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoogleButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(GlimpseDp.dp24),
            verticalArrangement = Arrangement.Center,
        ) {
            GlimpseGoogleButton(onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoogleButtonDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxSize().padding(GlimpseDp.dp24),
            verticalArrangement = Arrangement.Center,
        ) {
            GlimpseGoogleButton(onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccentButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.padding(GlimpseDp.dp32),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpseAccentButton(text = "Sign up", onClick = {})
        }
    }
}
