package com.example.githubapp.ui.githubscreen.data

sealed class IssuesViewIntent {
    data class LoadIssues(val owner: String, val repo: String, val state: String? = "open") : IssuesViewIntent()
    object Refresh : IssuesViewIntent()
}