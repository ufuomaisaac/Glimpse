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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpseLogoText
import com.example.glimpse.designsystem.components.GlimpseOtpInput
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.feature.auth.viewmodel.AuthUiState
import com.example.glimpse.feature.auth.viewmodel.AuthViewModel
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.verify_button
import glimpse.shared.generated.resources.verify_subtitle
import glimpse.shared.generated.resources.verify_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationScreen(
    signUpId: String,
    email: String,
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
            email = email,
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
    email: String,
    onCodeChange: (String) -> Unit,
    isLoading: Boolean,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        GlimpseLogoText()

        Spacer(Modifier.height(GlimpseDp.dp24))

        Text(
            text = stringResource(Res.string.verify_title),
            style = GlimpseTextStyles.headingH3,
            fontSize = GlimpseSp.sp24,
        )

        Spacer(Modifier.height(GlimpseDp.dp8))

        Text(
            text = stringResource(Res.string.verify_subtitle, email),
            style = GlimpseTextStyles.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpseOtpInput(
            value = code,
            onValueChange = onCodeChange,
            keyboardActions = KeyboardActions(
                onDone = { if (code.length == 6 && !isLoading) onVerify() },
            ),
        )

        Spacer(Modifier.height(GlimpseDp.dp32))

        GlimpsePrimaryButton(
            text = stringResource(Res.string.verify_button),
            onClick = onVerify,
            isLoading = isLoading,
            enabled = code.length == 6,
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun EmailVerificationEmptyPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "",
            email = "user@example.com",
            onCodeChange = {},
            isLoading = false,
            onVerify = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailVerificationFilledPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "123456",
            email = "user@example.com",
            onCodeChange = {},
            isLoading = false,
            onVerify = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailVerificationLoadingPreview() {
    GlimpseTheme {
        EmailVerificationContent(
            code = "123456",
            email = "user@example.com",
            onCodeChange = {},
            isLoading = true,
            onVerify = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailVerificationDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        EmailVerificationContent(
            code = "123456",
            email = "user@example.com",
            onCodeChange = {},
            isLoading = false,
            onVerify = {},
        )
    }
}
