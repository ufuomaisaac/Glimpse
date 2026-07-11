package com.example.glimpse.core.model

sealed interface SignUpOutcome {
    data object Complete : SignUpOutcome
    data class NeedsEmailVerification(val signUpId: String) : SignUpOutcome
}
