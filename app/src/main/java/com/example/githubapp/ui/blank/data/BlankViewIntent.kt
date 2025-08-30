package com.example.githubapp.ui.blank.data

/**
 * Blank screen ViewIntent for MVI architecture
 */
sealed class BlankViewIntent {
    object Load : BlankViewIntent()
    object Refresh : BlankViewIntent()
}