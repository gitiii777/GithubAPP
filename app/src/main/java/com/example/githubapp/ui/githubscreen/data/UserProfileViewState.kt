package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.User

/**
 * User profile screen ViewState for MVI architecture
 */
sealed class UserProfileViewState {
    object Loading : UserProfileViewState()
    data class Success(val user: User) : UserProfileViewState()
    data class Error(val message: String) : UserProfileViewState()
}