package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.Repository

/**
 * User repositories screen ViewState for MVI architecture
 */
sealed class UserRepoViewState {
    object Loading : UserRepoViewState()
    data class Success(val repositories: List<Repository>) : UserRepoViewState()
    data class Error(val message: String) : UserRepoViewState()
}