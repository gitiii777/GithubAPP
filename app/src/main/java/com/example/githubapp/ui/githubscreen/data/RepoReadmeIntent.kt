package com.example.githubapp.ui.githubscreen.data

sealed class RepoReadmeIntent {
    data class LoadReadme(val owner: String, val repo: String) : RepoReadmeIntent()
}