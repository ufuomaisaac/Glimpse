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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.glimpse.designsystem.AccentPrimary
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseDp.dp16
import com.example.glimpse.designsystem.GlimpseDp.dp32
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.designsystem.components.GlimpseSecondaryButton
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.get_stated_button
import glimpse.shared.generated.resources.get_stated_have_account
import glimpse.shared.generated.resources.get_stated_subtitle
import glimpse.shared.generated.resources.get_stated_title
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
            .padding(top = GlimpseDp.dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
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
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = AccentPrimary
        )


        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpsePrimaryButton(
            modifier = Modifier.padding(horizontal = dp32),
            text = stringResource(Res.string.get_stated_button),
            onClick = onGetStarted,
        )

        Spacer(Modifier.height(dp16))

        GlimpseSecondaryButton(
            modifier = Modifier.padding(horizontal = dp32),
            text = stringResource(Res.string.get_stated_have_account),
            onClick = onAlreadyHaveAccount,
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
