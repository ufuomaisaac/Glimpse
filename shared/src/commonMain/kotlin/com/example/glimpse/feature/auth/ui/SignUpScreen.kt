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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.AccentPrimary
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
import glimpse.shared.generated.resources.auth_email
import glimpse.shared.generated.resources.auth_have_account
import glimpse.shared.generated.resources.auth_legal_connector
import glimpse.shared.generated.resources.auth_legal_prefix
import glimpse.shared.generated.resources.auth_log_in
import glimpse.shared.generated.resources.auth_or_with_email
import glimpse.shared.generated.resources.auth_password
import glimpse.shared.generated.resources.auth_privacy_policy
import glimpse.shared.generated.resources.auth_sign_up
import glimpse.shared.generated.resources.auth_terms_of_service
import glimpse.shared.generated.resources.auth_username
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
            onSignUp = viewModel::signUp,
            onLogIn = onNavigateToSignIn,
            googleSignInEnabled = uiState !is AuthUiState.Loading,
            isLoading = uiState is AuthUiState.Loading,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun SignUpContent(
    onGoogleSignIn: () -> Unit = {},
    onSignUp: (email: String, password: String, username: String) -> Unit = { _, _, _ -> },
    onLogIn: () -> Unit = {},
    googleSignInEnabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val canSubmit = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()

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
                color = TextSecondary,
                style = GlimpseTextStyles.bodySmall.copy(fontSize = GlimpseSp.sp10),
                modifier = Modifier.padding(horizontal = GlimpseDp.dp12),
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = GlimpseDp.dp1,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
        Spacer(Modifier.height(GlimpseDp.dp24))

        Text(text = stringResource(Res.string.auth_username),
            style = GlimpseTextStyles.overline)

        Spacer(Modifier.height(GlimpseDp.dp8))

        GlimpseInputTextField(
            value = username,
            onValueChange = { username = it },
        )

        Spacer(Modifier.height(GlimpseDp.dp16))

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

        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpsePrimaryButton(
            text = stringResource(Res.string.auth_sign_up),
            onClick = { onSignUp(email, password, username) },
            isLoading = isLoading,
            enabled = canSubmit,
        )

        Spacer(Modifier.height(GlimpseDp.dp12))

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.auth_legal_prefix))
                withStyle(SpanStyle(color = AccentPrimary)) {
                    append(stringResource(Res.string.auth_terms_of_service))
                }
                append(stringResource(Res.string.auth_legal_connector))
                withStyle(SpanStyle(color = AccentPrimary)) {
                    append(stringResource(Res.string.auth_privacy_policy))
                }
                append(".")
            },
            style = GlimpseTextStyles.legal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(GlimpseDp.dp16))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.auth_have_account),
                style = GlimpseTextStyles.bodySmall,
                fontSize = GlimpseSp.sp12
            )
            Spacer(Modifier.width(GlimpseDp.dp4))
            GlimpseAccentButton(
                text = stringResource(Res.string.auth_log_in),
                onClick = onLogIn,
            )
        }

        Spacer(Modifier.height(GlimpseDp.dp32))

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
