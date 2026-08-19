package com.example.glimpse.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.glimpse.feature.auth.ui.EmailVerificationScreen
import com.example.glimpse.feature.auth.ui.GetStatedScreen
import com.example.glimpse.feature.auth.ui.SignInScreen
import com.example.glimpse.feature.auth.ui.SignUpScreen
import kotlinx.serialization.Serializable

@Serializable object AuthGraph
@Serializable object GetStated
@Serializable object SignIn
@Serializable object SignUp
@Serializable data class EmailVerification(val signUpId: String, val email: String)

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit,
    onFirstAuthentication: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = GetStated) {
        composable<GetStated> {
            GetStatedScreen(
                onGetStarted = {
                    navController.navigate(SignUp) { launchSingleTop = true }
                },
                onAlreadyHaveAccount = {
                    navController.navigate(SignIn) { launchSingleTop = true }
                },
            )
        }
        composable<SignIn> {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigate(SignUp) { launchSingleTop = true }
                },
                onSignedIn = onNavigateToMain,
            )
        }
        composable<SignUp> {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.navigate(SignIn) {
                        popUpTo(SignUp) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToEmailVerification = { signUpId, email ->
                    navController.navigate(EmailVerification(signUpId, email)) {
                        popUpTo(SignUp) { inclusive = true }
                    }
                },
                onSignedIn = onFirstAuthentication,
            )
        }
        composable<EmailVerification> { backStackEntry ->
            val route = backStackEntry.toRoute<EmailVerification>()
            EmailVerificationScreen(
                signUpId = route.signUpId,
                email = route.email,
                onVerified = onFirstAuthentication,
            )
        }
    }
}
