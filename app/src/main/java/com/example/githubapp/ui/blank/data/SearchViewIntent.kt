package com.example.githubapp.ui.blank.data

/**
 * Search screen ViewIntent for MVI architecture
 */
sealed class SearchViewIntent {
    data class Search(val query: String) : SearchViewIntent()
}