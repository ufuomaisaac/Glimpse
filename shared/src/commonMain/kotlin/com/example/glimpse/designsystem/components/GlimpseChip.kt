package com.example.glimpse.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.interFontFamily

@Composable
fun GlimpseChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier.size(
            width = GlimpseDp.dp120,
            height = GlimpseDp.dp50,
        ),
        shape = RoundedCornerShape(GlimpseDp.dp16),
        color = if (selected) colors.primaryContainer else colors.surface,
        contentColor = if (selected) colors.onPrimaryContainer else colors.onSurface,
        border = BorderStroke(
            width = GlimpseDp.dp1,
            color = if (selected) colors.primary else colors.outlineVariant,
        ),
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = GlimpseDp.dp21,
                vertical = GlimpseDp.dp18,
            ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                maxLines = 1,
                style = TextStyle(
                    fontFamily = interFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = GlimpseSp.sp13,
                    lineHeight = GlimpseSp.sp13,
                    letterSpacing = GlimpseSp.sp0,
                ),
            )
        }
    }
}
