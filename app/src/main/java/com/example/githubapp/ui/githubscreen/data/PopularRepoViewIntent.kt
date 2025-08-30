package com.example.githubapp.ui.githubscreen.data

sealed class PopularRepoViewIntent {
    object Load : PopularRepoViewIntent()
}