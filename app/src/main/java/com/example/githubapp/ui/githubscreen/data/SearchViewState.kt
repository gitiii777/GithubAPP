package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.Repository

/**
 * Search screen ViewState for MVI architecture
 */
sealed class SearchViewState {
    object Initial : SearchViewState()
    object Loading : SearchViewState()
    data class Success(val repositories: List<Repository>) : SearchViewState()
    data class Error(val message: String) : SearchViewState()
}