package com.example.githubapp.ui.githubscreen.data

/**
 * Login screen ViewState for MVI architecture
 */
sealed class LoginViewState {
    object Initial : LoginViewState()
    object Loading : LoginViewState()
    object Success : LoginViewState()
    data class Error(val message: String) : LoginViewState()
}