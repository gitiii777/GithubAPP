package com.example.githubapp.data.usecase

import com.example.githubapp.data.repository.Repository
import com.example.githubapp.data.repository.User

interface IGitApiController {
    suspend fun getRepositories(): List<Repository>
    suspend fun getRepositoryReadme(owner: String, repo: String, ref: String? = null): String
    suspend fun searchRepositories(
        query: String,
        sort: String? = null,
        order: String? = null
    ): List<Repository>
    suspend fun getAuthenticatedUser(): User
    suspend fun getUserRepositories(): List<Repository>
}