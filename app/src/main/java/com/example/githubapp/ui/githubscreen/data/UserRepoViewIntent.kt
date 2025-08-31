package com.example.githubapp.ui.githubscreen.data

/**
 * User repositories screen ViewIntent for MVI architecture
 */
sealed class UserRepoViewIntent {
    object Load : UserRepoViewIntent()
    object Refresh : UserRepoViewIntent()
}