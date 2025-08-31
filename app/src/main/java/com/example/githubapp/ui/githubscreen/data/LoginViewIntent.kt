package com.example.githubapp.ui.githubscreen.data

/**
 * Login screen ViewIntent for MVI architecture
 */
sealed class LoginViewIntent {
    data class Login(val username: String, val password: String) : LoginViewIntent()
}