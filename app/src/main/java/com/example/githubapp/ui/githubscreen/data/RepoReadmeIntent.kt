package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.IssueRequest

sealed class RepoReadmeIntent {
    data class LoadReadme(val owner: String, val repo: String) : RepoReadmeIntent()
    object ShowCreateIssueDialog : RepoReadmeIntent()
    object HideCreateIssueDialog : RepoReadmeIntent()
    data class CreateIssue(val owner: String, val repo: String, val issueRequest: IssueRequest) : RepoReadmeIntent()
    object ResetIssueCreationState : RepoReadmeIntent()
    object ShowOptionsMenu : RepoReadmeIntent()
    object HideOptionsMenu : RepoReadmeIntent()
}