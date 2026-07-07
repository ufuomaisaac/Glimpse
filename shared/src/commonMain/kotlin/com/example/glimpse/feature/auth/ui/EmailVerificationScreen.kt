package com.example.glimpse.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.Spacing
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.designsystem.components.GlimpseTextField
import com.example.glimpse.feature.auth.viewmodel.AuthUiState
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationScreen(
    signUpId: String,
    onVerified: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var code by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.SignedIn -> onVerified()
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearError()
            }
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        EmailVerificationContent(
            code = code,
            onCodeChange = { if (it.length <= 6) code = it.filter(Char::isDigit) },
            isLoading = uiState is AuthUiState.Loading,
            onVerify = { viewModel.verifyEmail(signUpId, code) },
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun EmailVerificationContent(
    code: String,
    onCodeChange: (String) -> Unit,
    isLoading: Boolean,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Check your email", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(Spacing.dp8))
        Text(
            "Enter the 6-digit code we sent you.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(Spacing.dp32))

        GlimpseTextField(
            value = code,
            onValueChange = onCodeChange,
            placeholder = "6-digit code",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
        )
        Spacer(Modifier.height(Spacing.dp24))

        GlimpsePrimaryButton(
            text = "Verify",
            onClick = onVerify,
            isLoading = isLoading,
            enabled = code.length == 6,
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
private fun EmailVerificationEmptyPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "",
            onCodeChange = {},
            isLoading = false,
            onVerify = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationFilledPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "123456",
            onCodeChange = {},
            isLoading = false,
            onVerify = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationLoadingPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "123456",
            onCodeChange = {},
            isLoading = true,
            onVerify = {},
        )
    }
}
