package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.Issue

data class IssuesViewState(
    val isLoading: Boolean = false,
    val issues: List<Issue> = emptyList(),
    val error: String = ""
)