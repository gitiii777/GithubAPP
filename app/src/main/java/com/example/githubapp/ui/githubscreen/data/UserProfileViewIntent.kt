package com.example.githubapp.ui.githubscreen.data

/**
 * User profile screen ViewIntent for MVI architecture
 */
sealed class UserProfileViewIntent {
    object Load : UserProfileViewIntent()
    object Refresh : UserProfileViewIntent()
}