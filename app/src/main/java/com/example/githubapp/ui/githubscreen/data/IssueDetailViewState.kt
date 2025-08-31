package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.Issue

data class IssueDetailViewState(
    val isLoading: Boolean = false,
    val issue: Issue? = null,
    val error: String = ""
)