package com.example.githubapp.ui.data

/**
 * Blank screen ViewState for MVI architecture
 */
sealed class BlankViewState {
    object Loading : BlankViewState()
    object Success : BlankViewState()
    data class Error(val message: String) : BlankViewState()
}