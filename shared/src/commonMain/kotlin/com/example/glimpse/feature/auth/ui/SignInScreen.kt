package com.example.glimpse.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.TextSecondary
import com.example.glimpse.designsystem.components.GlimpseAccentButton
import com.example.glimpse.designsystem.components.GlimpseGoogleButton
import com.example.glimpse.designsystem.components.GlimpseInputTextField
import com.example.glimpse.designsystem.components.GlimpseLogoText
import com.example.glimpse.designsystem.components.GlimpsePasswordInputTextField
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.feature.auth.viewmodel.AuthUiState
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.auth_create_an_account
import glimpse.shared.generated.resources.auth_email
import glimpse.shared.generated.resources.auth_forgotten_password
import glimpse.shared.generated.resources.auth_new_here
import glimpse.shared.generated.resources.auth_or_with_email
import glimpse.shared.generated.resources.auth_password
import glimpse.shared.generated.resources.auth_sign_in
import glimpse.shared.generated.resources.sign_in_subtitle
import glimpse.shared.generated.resources.sign_in_title
import glimpse.shared.generated.resources.sign_in_title_continuation
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onSignedIn: () -> Unit,
    onForgottenPassword: () -> Unit = {},
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.SignedIn -> onSignedIn()
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearError()
            }
            else -> Unit
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        SignInContent(
            onGoogleSignIn = viewModel::continueWithGoogle,
            onSignIn = viewModel::signIn,
            onCreateAccount = onNavigateToSignUp,
            onForgottenPassword = onForgottenPassword,
            googleSignInEnabled = uiState !is AuthUiState.Loading,
            isLoading = uiState is AuthUiState.Loading,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun SignInContent(
    onGoogleSignIn: () -> Unit = {},
    onSignIn: (email: String, password: String) -> Unit = { _, _ -> },
    onCreateAccount: () -> Unit = {},
    onForgottenPassword: () -> Unit = {},
    googleSignInEnabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val canSubmit = email.isNotBlank() && password.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(
                top = GlimpseDp.dp116,
                start = GlimpseDp.dp16,
                end = GlimpseDp.dp16,
            ),
    ) {
        GlimpseLogoText()

        Spacer(Modifier.height(GlimpseDp.dp24))

        Text(
            text = stringResource(Res.string.sign_in_title),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24
        )

        Spacer(Modifier.height(GlimpseDp.dp2))

        Text(
            text = stringResource(Res.string.sign_in_title_continuation),
            style = GlimpseTextStyles.headingH3,
            textAlign = TextAlign.Start,
            fontSize = GlimpseSp.sp24
        )

        Spacer(Modifier.height(GlimpseDp.dp4))

        Text(
            text = stringResource(Res.string.sign_in_subtitle),
            style = GlimpseTextStyles.labelMedium,
            fontSize = GlimpseSp.sp10
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
                color = TextSecondary,
                style = GlimpseTextStyles.overline,
                modifier = Modifier.padding(horizontal = GlimpseDp.dp12),
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = GlimpseDp.dp1,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }

        Spacer(Modifier.height(GlimpseDp.dp24))

        Text(text = stringResource(Res.string.auth_email),
            style = GlimpseTextStyles.overline)

        Spacer(Modifier.height(GlimpseDp.dp16))

        GlimpseInputTextField(
            value = email,
            onValueChange = { email = it },
        )

        Spacer(Modifier.height(GlimpseDp.dp16))

        Text(text = stringResource(Res.string.auth_password),
            style = GlimpseTextStyles.overline)

        Spacer(Modifier.height(GlimpseDp.dp16))

        GlimpsePasswordInputTextField(
            value = password,
            onValueChange = { password = it },
        )

        Spacer(Modifier.height(GlimpseDp.dp12))

        GlimpseAccentButton(
            text = stringResource(Res.string.auth_forgotten_password),
            onClick = onForgottenPassword,
            modifier = Modifier.align(Alignment.End),
        )

        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpsePrimaryButton(
            text = stringResource(Res.string.auth_sign_in),
            onClick = { onSignIn(email, password) },
            isLoading = isLoading,
            enabled = canSubmit,
        )

        Spacer(Modifier.height(GlimpseDp.dp16))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.auth_new_here),
                style = GlimpseTextStyles.bodySmall,
                fontSize = GlimpseSp.sp12
            )
            Spacer(Modifier.width(GlimpseDp.dp4))
            GlimpseAccentButton(
                text = stringResource(Res.string.auth_create_an_account),
                onClick = onCreateAccount,
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SignInContentPreview() {
    GlimpseTheme {
        SignInContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInContentDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        SignInContent()
    }
}
