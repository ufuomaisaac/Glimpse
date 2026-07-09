package com.example.glimpse.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpseAccentButton
import com.example.glimpse.designsystem.components.GlimpseInputTextField
import com.example.glimpse.designsystem.components.GlimpsePasswordInputTextField
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.feature.auth.viewmodel.AuthUiState
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.auth_email_placeholder
import glimpse.shared.generated.resources.auth_have_account
import glimpse.shared.generated.resources.auth_no_account
import glimpse.shared.generated.resources.auth_password_placeholder
import glimpse.shared.generated.resources.auth_sign_in
import glimpse.shared.generated.resources.auth_sign_in_title
import glimpse.shared.generated.resources.auth_sign_up
import glimpse.shared.generated.resources.auth_sign_up_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.SignedIn -> onSignedIn()
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as AuthUiState.Error).message)
                viewModel.clearError()
            }
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        AuthForm(
            title = stringResource(Res.string.auth_sign_in_title),
            submitLabel = stringResource(Res.string.auth_sign_in),
            isLoading = uiState is AuthUiState.Loading,
            onSubmit = viewModel::signIn,
            footerText = stringResource(Res.string.auth_no_account),
            footerActionText = stringResource(Res.string.auth_sign_up),
            onFooterAction = onNavigateToSignUp,
            modifier = Modifier.padding(padding),
        )
    }
}

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
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        AuthForm(
            title = stringResource(Res.string.auth_sign_up_title),
            submitLabel = stringResource(Res.string.auth_sign_up),
            isLoading = uiState is AuthUiState.Loading,
            onSubmit = viewModel::signUp,
            footerText = stringResource(Res.string.auth_have_account),
            footerActionText = stringResource(Res.string.auth_sign_in),
            onFooterAction = onNavigateToSignIn,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun AuthForm(
    title: String,
    submitLabel: String,
    isLoading: Boolean,
    onSubmit: (email: String, password: String) -> Unit,
    footerText: String,
    footerActionText: String,
    onFooterAction: () -> Unit,
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
            .padding(horizontal = GlimpseDp.dp24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpseInputTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(Res.string.auth_email_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(GlimpseDp.dp16))

        GlimpsePasswordInputTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(Res.string.auth_password_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (canSubmit && !isLoading) onSubmit(email, password) },
            ),
        )
        Spacer(Modifier.height(GlimpseDp.dp24))

        GlimpsePrimaryButton(
            text = submitLabel,
            onClick = { onSubmit(email, password) },
            isLoading = isLoading,
            enabled = canSubmit,
        )
        Spacer(Modifier.height(GlimpseDp.dp16))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(footerText, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(GlimpseDp.dp4))
            GlimpseAccentButton(text = footerActionText, onClick = onFooterAction)
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    GlimpseTheme {
        AuthForm(
            title = stringResource(Res.string.auth_sign_in_title),
            submitLabel = stringResource(Res.string.auth_sign_in),
            isLoading = false,
            onSubmit = { _, _ -> },
            footerText = stringResource(Res.string.auth_no_account),
            footerActionText = stringResource(Res.string.auth_sign_up),
            onFooterAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    GlimpseTheme {
        AuthForm(
            title = stringResource(Res.string.auth_sign_up_title),
            submitLabel = stringResource(Res.string.auth_sign_up),
            isLoading = false,
            onSubmit = { _, _ -> },
            footerText = stringResource(Res.string.auth_have_account),
            footerActionText = stringResource(Res.string.auth_sign_in),
            onFooterAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInLoadingPreview() {
    GlimpseTheme {
        AuthForm(
            title = stringResource(Res.string.auth_sign_in_title),
            submitLabel = stringResource(Res.string.auth_sign_in),
            isLoading = true,
            onSubmit = { _, _ -> },
            footerText = stringResource(Res.string.auth_no_account),
            footerActionText = stringResource(Res.string.auth_sign_up),
            onFooterAction = {},
        )
    }
}
