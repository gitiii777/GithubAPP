package com.example.githubapp.ui.githubscreen.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.data.repository.AuthManager

class LoginViewModelFactory(
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}