package com.example.githubapp.data.usecase

import com.example.githubapp.data.repository.Repository

interface IGitApiController {
    suspend fun getRepositories(): List<Repository>
    suspend fun getRepositoryReadme(owner: String, repo: String, ref: String? = null): String
}