package com.example.glimpse.designsystem

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.dm_sans_bold
import glimpse.shared.generated.resources.dm_sans_bold_italic
import glimpse.shared.generated.resources.dm_sans_italic
import glimpse.shared.generated.resources.dm_sans_medium
import glimpse.shared.generated.resources.dm_sans_medium_italic
import glimpse.shared.generated.resources.dm_sans_regular
import glimpse.shared.generated.resources.eras_demi_itc
import glimpse.shared.generated.resources.jetbrains_mono_bold
import glimpse.shared.generated.resources.jetbrains_mono_medium
import glimpse.shared.generated.resources.jetbrains_mono_regular
import org.jetbrains.compose.resources.Font

@Composable
fun erasDemiFontFamily() = FontFamily(
    Font(Res.font.eras_demi_itc, FontWeight.SemiBold),
)

@Composable
fun dmSansFontFamily() = FontFamily(
    Font(Res.font.dm_sans_regular, FontWeight.Normal),
    Font(Res.font.dm_sans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.dm_sans_medium, FontWeight.Medium),
    Font(Res.font.dm_sans_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.dm_sans_bold, FontWeight.Bold),
    Font(Res.font.dm_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
)

@Composable
fun jetBrainsMonoFontFamily() = FontFamily(
    Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
    Font(Res.font.jetbrains_mono_bold, FontWeight.Bold),
)

@Composable
fun glimpseTypography(): Typography {
    val erasDemi = erasDemiFontFamily()
    val dmSans = dmSansFontFamily()
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        displayMedium = base.displayMedium.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        displaySmall = base.displaySmall.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        headlineLarge = base.headlineLarge.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        headlineMedium = base.headlineMedium.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        headlineSmall = base.headlineSmall.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        titleSmall = base.titleSmall.copy(fontFamily = erasDemi, fontWeight = FontWeight.SemiBold),
        bodyLarge = base.bodyLarge.copy(fontFamily = dmSans),
        bodyMedium = base.bodyMedium.copy(fontFamily = dmSans),
        bodySmall = base.bodySmall.copy(fontFamily = dmSans),
        labelLarge = base.labelLarge.copy(fontFamily = dmSans, fontWeight = FontWeight.Medium),
        labelMedium = base.labelMedium.copy(fontFamily = dmSans, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = dmSans, fontWeight = FontWeight.Medium),
    )
}
