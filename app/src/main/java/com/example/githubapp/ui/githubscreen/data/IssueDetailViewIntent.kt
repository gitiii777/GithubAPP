package com.example.githubapp.ui.githubscreen.data

sealed class IssueDetailViewIntent {
    data class LoadIssue(val owner: String, val repo: String, val issueNumber: Int) : IssueDetailViewIntent()
}