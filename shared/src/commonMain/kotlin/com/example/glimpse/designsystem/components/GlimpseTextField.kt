package com.example.glimpse.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glimpse.designsystem.AccentPrimary
import com.example.glimpse.designsystem.BorderLight
import com.example.glimpse.designsystem.BorderWidth
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.Radius
import com.example.glimpse.designsystem.Spacing
import com.example.glimpse.designsystem.Surface
import com.example.glimpse.designsystem.TextPrimary
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GlimpseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val shape = RoundedCornerShape(Radius.dp12)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(Surface)
            .border(BorderWidth.dp1, BorderLight, shape),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp16),
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = AccentPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                innerTextField()
            }
        },
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
        ) {
            GlimpseTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email address",
            )
            GlimpseTextField(
                value = "user@example.com",
                onValueChange = {},
                placeholder = "Email address",
            )
            GlimpseTextField(
                value = "",
                onValueChange = {},
                placeholder = "Password",
                isPassword = true,
            )
        }
    }
}
