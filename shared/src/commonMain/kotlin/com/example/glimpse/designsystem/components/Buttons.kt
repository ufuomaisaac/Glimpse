package com.example.glimpse.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glimpse.designsystem.AccentPrimary
import com.example.glimpse.designsystem.BorderLight
import com.example.glimpse.designsystem.BorderWidth
import com.example.glimpse.designsystem.Elevation
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.Radius
import com.example.glimpse.designsystem.Spacing
import com.example.glimpse.designsystem.Surface
import com.example.glimpse.designsystem.TextPrimary
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GlimpsePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(Radius.dp28)

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentPrimary,
            contentColor = Surface,
            disabledContainerColor = AccentPrimary.copy(alpha = 0.5f),
            disabledContentColor = Surface.copy(alpha = 0.7f),
        ),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = Spacing.dp16),
        modifier = modifier
            .shadow(
                elevation = Elevation.dp8,
                shape = shape,
                spotColor = AccentPrimary.copy(alpha = 0.25f),
                ambientColor = AccentPrimary.copy(alpha = 0.10f),
            )
            .width(206.dp)
            .height(54.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = BorderWidth.dp2,
                color = Surface,
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun GlimpseSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(Radius.dp28),
        border = BorderStroke(BorderWidth.dp1, BorderLight),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Surface,
            contentColor = TextPrimary,
        ),
        modifier = modifier
            .width(311.dp)
            .height(56.dp),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
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
        color = AccentPrimary,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
private fun PrimaryButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpsePrimaryButton(text = "Sign in", onClick = {})
            GlimpsePrimaryButton(text = "Loading", onClick = {}, isLoading = true)
            GlimpsePrimaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpseSecondaryButton(text = "Continue with Google", onClick = {})
            GlimpseSecondaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview
@Composable
private fun AccentButtonPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(Spacing.dp24),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GlimpseAccentButton(text = "Sign up", onClick = {})
        }
    }
}
