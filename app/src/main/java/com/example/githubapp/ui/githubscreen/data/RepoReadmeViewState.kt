package com.example.githubapp.ui.githubscreen.data

data class RepoReadmeViewState(
    val isLoading: Boolean = false,
    val readmeContent: String = "",
    val error: String = "",
    val showCreateIssueDialog: Boolean = false,
    val issueCreationLoading: Boolean = false,
    val issueCreationSuccess: Boolean = false,
    val issueCreationError: String = "",
    val showOptionsMenu: Boolean = false
)