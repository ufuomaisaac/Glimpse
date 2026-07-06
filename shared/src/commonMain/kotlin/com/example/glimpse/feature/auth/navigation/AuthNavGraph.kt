package com.example.glimpse.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.glimpse.feature.auth.ui.EmailVerificationScreen
import com.example.glimpse.feature.auth.ui.SignInScreen
import com.example.glimpse.feature.auth.ui.SignUpScreen
import kotlinx.serialization.Serializable

@Serializable object AuthGraph
@Serializable object SignIn
@Serializable object SignUp
@Serializable data class EmailVerification(val signUpId: String)

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = SignIn) {
        composable<SignIn> {
            SignInScreen(
                onNavigateToSignUp = { navController.navigate(SignUp) },
                onSignedIn = onNavigateToMain,
            )
        }
        composable<SignUp> {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigate(SignIn) },
                onNavigateToEmailVerification = { signUpId ->
                    navController.navigate(EmailVerification(signUpId)) {
                        popUpTo(SignUp) { inclusive = true }
                    }
                },
                onSignedIn = onNavigateToMain,
            )
        }
        composable<EmailVerification> { backStackEntry ->
            val route = backStackEntry.toRoute<EmailVerification>()
            EmailVerificationScreen(
                signUpId = route.signUpId,
                onVerified = onNavigateToMain,
            )
        }
    }
}
