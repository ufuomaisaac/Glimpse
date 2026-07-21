package com.example.glimpse.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.auth_continue_with_google
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
            text = stringResource(Res.string.auth_continue_with_google),
            style = MaterialTheme.typography.labelLarge,
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
