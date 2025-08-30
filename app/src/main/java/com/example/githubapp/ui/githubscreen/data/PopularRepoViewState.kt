package com.example.githubapp.ui.githubscreen.data

import com.example.githubapp.data.repository.Repository

data class PopularRepoViewState(
    val isLoading: Boolean = false,
    val message: String = "",
    val repositories: List<Repository> = emptyList(),
)