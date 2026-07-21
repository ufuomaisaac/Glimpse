package com.example.glimpse.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseDp.dp16
import com.example.glimpse.designsystem.GlimpseDp.dp32
import com.example.glimpse.designsystem.GlimpseIcons
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpseIconTextRow
import com.example.glimpse.designsystem.components.GlimpseLogoText
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.get_stated_button
import glimpse.shared.generated.resources.get_stated_feature_matching
import glimpse.shared.generated.resources.get_stated_feature_private
import glimpse.shared.generated.resources.get_stated_feature_sharing
import glimpse.shared.generated.resources.get_stated_no_account_needed_to_receive_photos
import glimpse.shared.generated.resources.get_stated_subtitle
import glimpse.shared.generated.resources.get_stated_title
import glimpse.shared.generated.resources.get_stated_title_continuation
import glimpse.shared.generated.resources.welcome_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GetStatedScreen(
    onGetStarted: () -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = GlimpseDp.dp116, start = GlimpseDp.dp16, end = GlimpseDp.dp16),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        GlimpseLogoText()

        Spacer(Modifier.height(GlimpseDp.dp32))

        Image(
            painter = painterResource(Res.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(GlimpseDp.dp240),
        )
        Spacer(Modifier.height(GlimpseDp.dp16))

        Text(
            text = stringResource(Res.string.get_stated_title),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24
        )
        Spacer(Modifier.height(GlimpseDp.dp2))

        Text(
            text = stringResource(Res.string.get_stated_title_continuation),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24
        )
        Spacer(Modifier.height(GlimpseDp.dp8))

        Text(
            text = stringResource(Res.string.get_stated_subtitle),
            style = GlimpseTextStyles.bodySmall,
            fontSize = GlimpseSp.sp10

        )

        Spacer(Modifier.height(GlimpseDp.dp16))

        Column(
            verticalArrangement = Arrangement.spacedBy(GlimpseDp.dp12),
        ) {
            GlimpseIconTextRow(
                icon = GlimpseIcons.BoostLight,
                darkThemeIcon = GlimpseIcons.BoostDark,
                text = stringResource(Res.string.get_stated_feature_matching),
            )
            GlimpseIconTextRow(
                icon = GlimpseIcons.ShareLight,
                darkThemeIcon = GlimpseIcons.ShareDark,
                text = stringResource(Res.string.get_stated_feature_sharing),
            )
            GlimpseIconTextRow(
                icon = GlimpseIcons.ShieldLight,
                darkThemeIcon = GlimpseIcons.ShieldDark,
                text = stringResource(Res.string.get_stated_feature_private),
            )
        }

        Spacer(Modifier.height(GlimpseDp.dp16))

        GlimpsePrimaryButton(
            modifier = Modifier,
            text = stringResource(Res.string.get_stated_button),
            onClick = onGetStarted,
        )

        Spacer(Modifier.height(GlimpseDp.dp8))


        Text(
            text = stringResource(Res.string.get_stated_no_account_needed_to_receive_photos),
            style = GlimpseTextStyles.bodySmall,
            fontSize = GlimpseSp.sp10,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }


}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun GetStatedScreenPreview() {
    GlimpseTheme {
        GetStatedScreen()
    }
}
