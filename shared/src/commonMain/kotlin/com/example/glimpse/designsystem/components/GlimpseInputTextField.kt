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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseIcons
import com.example.glimpse.designsystem.GlimpseTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun GlimpseInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    GlimpseBaseInputTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun GlimpsePasswordInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    GlimpseBaseInputTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingContent = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(GlimpseIcons.Eye),
                    contentDescription = "Toggle password visibility",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(GlimpseDp.dp20),
                )
            }
        },
    )
}

@Composable
private fun GlimpseBaseInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(GlimpseDp.dp12)
    val endPadding = if (trailingContent == null) GlimpseDp.dp16 else GlimpseDp.dp48
    val inputTextStyle = MaterialTheme.typography.bodyMedium
    val colors = MaterialTheme.colorScheme

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = inputTextStyle.merge(
            TextStyle(
                color = colors.onSurface,
                textAlign = TextAlign.Start,
            ),
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(GlimpseDp.dp52)
            .clip(shape)
            .background(colors.surface)
            .border(GlimpseDp.dp1, colors.outlineVariant, shape),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = GlimpseDp.dp16, end = endPadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.outline,
                            style = inputTextStyle,
                        )
                    }
                    innerTextField()
                }

                if (trailingContent != null) {
                    Box(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        contentAlignment = Alignment.Center,
                    ) {
                        trailingContent()
                    }
                }
            }
        },
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun InputTextFieldPreview() {
    GlimpseTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(GlimpseDp.dp24),
            verticalArrangement = Arrangement.spacedBy(GlimpseDp.dp16),
        ) {
            GlimpseInputTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email address",
            )
            GlimpseInputTextField(
                value = "user@example.com",
                onValueChange = {},
                placeholder = "Email address",
            )
            GlimpsePasswordInputTextField(
                value = "password123",
                onValueChange = {},
                placeholder = "Password",
            )
        }
    }
}
