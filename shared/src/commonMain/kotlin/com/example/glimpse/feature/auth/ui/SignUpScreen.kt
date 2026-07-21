package com.example.glimpse.feature.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpseGoogleButton
import com.example.glimpse.designsystem.components.GlimpseLogoText
import com.example.glimpse.feature.auth.viewmodel.AuthUiState
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.auth_or_with_email
import glimpse.shared.generated.resources.sign_up_subtitle
import glimpse.shared.generated.resources.sign_up_title
import glimpse.shared.generated.resources.sign_up_title_continuation
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToEmailVerification: (signUpId: String, email: String) -> Unit,
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.SignedIn -> onSignedIn()
            is AuthUiState.AwaitingEmailVerification -> {
                viewModel.clearError()
                onNavigateToEmailVerification(state.signUpId, state.email)
            }
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearError()
            }
            else -> Unit
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        SignUpContent(
            onGoogleSignIn = viewModel::continueWithGoogle,
            googleSignInEnabled = uiState !is AuthUiState.Loading,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun SignUpContent(
    onGoogleSignIn: () -> Unit = {},
    googleSignInEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = GlimpseDp.dp116,
                start = GlimpseDp.dp16,
                end = GlimpseDp.dp16,
            ),
    ) {
        GlimpseLogoText()

        Spacer(Modifier.height(GlimpseDp.dp24))

        Text(
            text = stringResource(Res.string.sign_up_title),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24,
        )

        Spacer(Modifier.height(GlimpseDp.dp2))

        Text(
            text = stringResource(Res.string.sign_up_title_continuation),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24,
        )

        Spacer(Modifier.height(GlimpseDp.dp4))

        Text(
            text = stringResource(Res.string.sign_up_subtitle),
            style = GlimpseTextStyles.labelMedium,
            fontSize = GlimpseSp.sp10,
        )

        Spacer(Modifier.height(GlimpseDp.dp24))

        GlimpseGoogleButton(
            onClick = onGoogleSignIn,
            enabled = googleSignInEnabled,
        )

        Spacer(Modifier.height(GlimpseDp.dp24))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = GlimpseDp.dp1,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Text(
                text = stringResource(Res.string.auth_or_with_email),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = GlimpseTextStyles.labelMedium,
                modifier = Modifier.padding(horizontal = GlimpseDp.dp12),
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = GlimpseDp.dp1,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentPreview() {
    GlimpseTheme {
        SignUpContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        SignUpContent()
    }
}
