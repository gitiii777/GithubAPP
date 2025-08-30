package com.example.githubapp.ui.githubscreen.data

data class RepoReadmeViewState(
    val isLoading: Boolean = false,
    val readmeContent: String = "",
    val error: String = ""
)