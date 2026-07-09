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
import com.example.glimpse.designsystem.AccentPrimary
import com.example.glimpse.designsystem.BorderLight
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTheme
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
    val shape = RoundedCornerShape(GlimpseDp.dp28)

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
        contentPadding = PaddingValues(horizontal = GlimpseDp.dp28, vertical = GlimpseDp.dp16),
        modifier = modifier
            .shadow(
                elevation = GlimpseDp.dp8,
                shape = shape,
                spotColor = AccentPrimary.copy(alpha = 0.25f),
                ambientColor = AccentPrimary.copy(alpha = 0.10f),
            )
            .fillMaxWidth()
            .height(GlimpseDp.dp54),
    ) {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = GlimpseSp.sp14,
            )
    }
}

@Composable
fun GlimpseSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(GlimpseDp.dp28),
        border = BorderStroke(GlimpseDp.dp2, BorderLight),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Surface,
            contentColor = TextPrimary,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(GlimpseDp.dp56),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp14,
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
        fontSize = GlimpseSp.sp14,
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
            GlimpsePrimaryButton(text = "Disabled", onClick = {})
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
