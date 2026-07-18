package com.example.glimpse.designsystem

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    val headingXl
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = GlimpseSp.sp28,
        )

    val headingLarge
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = GlimpseSp.sp22,
        )

    val headingMedium
        @Composable get() = TextStyle(
            fontFamily = spaceGroteskFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = GlimpseSp.sp18,
        )

    val bodyRegular
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = GlimpseSp.sp16,
        )

    val bodySmall
        @Composable get() = TextStyle(
            fontFamily = interFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp14,
        )

    val labelMono
        @Composable get() = TextStyle(
            fontFamily = jetBrainsMonoFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = GlimpseSp.sp11,
        )
}

@Composable
fun glimpseTypography(): Typography {
    val heading = spaceGroteskFontFamily()
    val body = interFontFamily()

    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        displayMedium = base.displayMedium.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        displaySmall = base.displaySmall.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineLarge = base.headlineLarge.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineMedium = base.headlineMedium.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineSmall = base.headlineSmall.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontFamily = heading, fontWeight = FontWeight.Medium),
        titleSmall = base.titleSmall.copy(fontFamily = heading, fontWeight = FontWeight.Medium),
        bodyLarge = base.bodyLarge.copy(fontFamily = body),
        bodyMedium = base.bodyMedium.copy(fontFamily = body),
        bodySmall = base.bodySmall.copy(fontFamily = body),
        labelLarge = base.labelLarge.copy(fontFamily = body, fontWeight = FontWeight.Medium),
        labelMedium = base.labelMedium.copy(fontFamily = body, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = body, fontWeight = FontWeight.Medium),
    )
}
