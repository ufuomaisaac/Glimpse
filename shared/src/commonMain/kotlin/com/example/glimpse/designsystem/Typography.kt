package com.example.glimpse.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.dm_sans_regular
import glimpse.shared.generated.resources.eras_demi_itc
import glimpse.shared.generated.resources.inter_bold
import glimpse.shared.generated.resources.inter_medium
import glimpse.shared.generated.resources.inter_regular
import glimpse.shared.generated.resources.inter_semibold
import glimpse.shared.generated.resources.jetbrains_mono_bold
import glimpse.shared.generated.resources.jetbrains_mono_medium
import glimpse.shared.generated.resources.jetbrains_mono_regular
import glimpse.shared.generated.resources.manrope_regular
import glimpse.shared.generated.resources.space_grotesk_bold
import glimpse.shared.generated.resources.space_grotesk_medium
import glimpse.shared.generated.resources.space_grotesk_regular
import glimpse.shared.generated.resources.space_grotesk_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun erasDemiFontFamily() = FontFamily(
    Font(Res.font.eras_demi_itc, FontWeight.Normal),
)

@Composable
fun dmSansFontFamily() = FontFamily(
    Font(Res.font.dm_sans_regular, FontWeight.Normal),
)

@Composable
fun manropeFontFamily() = FontFamily(
    Font(Res.font.manrope_regular, FontWeight.Normal),
)

@Composable
fun interFontFamily() = FontFamily(
    Font(Res.font.inter_regular, FontWeight.Normal),
    Font(Res.font.inter_medium, FontWeight.Medium),
    Font(Res.font.inter_semibold, FontWeight.SemiBold),
    Font(Res.font.inter_bold, FontWeight.Bold),
)

@Composable
fun spaceGroteskFontFamily() = FontFamily(
    Font(Res.font.space_grotesk_regular, FontWeight.Normal),
    Font(Res.font.space_grotesk_medium, FontWeight.Medium),
    Font(Res.font.space_grotesk_semibold, FontWeight.SemiBold),
    Font(Res.font.space_grotesk_bold, FontWeight.Bold),
)

@Composable
fun jetBrainsMonoFontFamily() = FontFamily(
    Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
    Font(Res.font.jetbrains_mono_bold, FontWeight.Bold),
)

object GlimpseTextStyles {
    val headingH1
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = GlimpseSp.sp32,
            lineHeight = GlimpseSp.sp32,
            letterSpacing = 0.sp,
        )

    val headingH2
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp24,
            lineHeight = GlimpseSp.sp24,
            letterSpacing = 0.sp,
        )

    val headingH3
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp20,
            lineHeight = GlimpseSp.sp20,
            letterSpacing = 0.sp,
        )

    val bodyRegular
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = GlimpseSp.sp16,
            lineHeight = GlimpseSp.sp16,
            letterSpacing = 0.sp,
        )

    val bodySmall
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = GlimpseSp.sp14,
            lineHeight = GlimpseSp.sp14,
            letterSpacing = 0.sp,
        )

    val caption
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = GlimpseSp.sp12,
            lineHeight = GlimpseSp.sp12,
            letterSpacing = 0.sp,
        )

    val labelMedium
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp11,
            lineHeight = GlimpseSp.sp11,
            letterSpacing = 0.sp,
        )

    val overline
        @Composable get() = TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = GlimpseSp.sp10,
            lineHeight = GlimpseSp.sp15,
            letterSpacing = 0.9.sp,
        )

    val legal
        @Composable get() = TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp10,
            lineHeight = GlimpseSp.sp10,
            letterSpacing = 0.sp,
        )
}

@Composable
fun glimpseTypography(): Typography {
    val heading = spaceGroteskFontFamily()
    val body = interFontFamily()

    val h1 = TextStyle(
        fontFamily = heading,
        fontWeight = FontWeight.Bold,
        fontSize = GlimpseSp.sp32,
        lineHeight = GlimpseSp.sp32,
        letterSpacing = 0.sp,
    )
    val h2 = TextStyle(
        fontFamily = heading,
        fontWeight = FontWeight.Bold,
        fontSize = GlimpseSp.sp24,
        lineHeight = GlimpseSp.sp24,
        letterSpacing = 0.sp,
    )
    val h3 = TextStyle(
        fontFamily = heading,
        fontWeight = FontWeight.Medium,
        fontSize = GlimpseSp.sp18,
        lineHeight = GlimpseSp.sp18,
        letterSpacing = 0.sp,
    )
    val bodyRegular = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Normal,
        fontSize = GlimpseSp.sp16,
        lineHeight = GlimpseSp.sp16,
        letterSpacing = 0.sp,
    )
    val bodySmall = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Normal,
        fontSize = GlimpseSp.sp14,
        lineHeight = GlimpseSp.sp14,
        letterSpacing = 0.sp,
    )
    val labelMedium = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Medium,
        fontSize = GlimpseSp.sp13,
        lineHeight = GlimpseSp.sp13,
        letterSpacing = 0.sp,
    )
    val caption = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Normal,
        fontSize = GlimpseSp.sp12,
        lineHeight = GlimpseSp.sp12,
        letterSpacing = 0.sp,
    )

    return Typography(
        displayLarge = h1,
        displayMedium = h1,
        displaySmall = h2,
        headlineLarge = h1,
        headlineMedium = h2,
        headlineSmall = h3,
        titleLarge = h1,
        titleMedium = h2,
        titleSmall = h3,
        bodyLarge = bodyRegular,
        bodyMedium = bodyRegular,
        bodySmall = bodySmall,
        labelLarge = labelMedium,
        labelMedium = labelMedium,
        labelSmall = caption,
    )
}
