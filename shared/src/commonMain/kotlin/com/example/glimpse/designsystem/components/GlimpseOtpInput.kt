package com.example.glimpse.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseTextStyles

private const val VerificationCodeLength = 6

@Composable
fun GlimpseOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(GlimpseDp.dp12)
    val isDarkTheme = colors.background.luminance() < 0.5f
    val containerColor = if (isDarkTheme) colors.surface else colors.background
    val shadowColor = Color(0x59000000)

    BasicTextField(
        value = value,
        onValueChange = { input ->
            onValueChange(input.filter(Char::isDigit).take(VerificationCodeLength))
        },
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = keyboardActions,
        textStyle = TextStyle(color = Color.Transparent),
        cursorBrush = SolidColor(Color.Transparent),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(GlimpseDp.dp8),
                ) {
                    repeat(VerificationCodeLength) { index ->
                        val isActive = value.length < VerificationCodeLength && index == value.length
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(GlimpseDp.dp52)
                                .dropShadow(
                                    shape = shape,
                                    shadow = Shadow(
                                        radius = GlimpseDp.dp3,
                                        color = shadowColor,
                                        offset = DpOffset(
                                            x = GlimpseDp.dp0,
                                            y = GlimpseDp.dp1,
                                        ),
                                    ),
                                )
                                .clip(shape)
                                .background(containerColor)
                                .border(
                                    width = GlimpseDp.dp1,
                                    color = if (isActive) colors.primary else colors.outlineVariant,
                                    shape = shape,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = value.getOrNull(index)?.toString().orEmpty(),
                                color = colors.onSurface,
                                style = GlimpseTextStyles.headingH3,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f),
                ) {
                    innerTextField()
                }
            }
        },
    )
}
