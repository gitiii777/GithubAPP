package com.example.githubapp.data.usecase

import android.util.Log
import com.example.githubapp.data.repository.GithubApiClient
import com.example.githubapp.data.repository.Repository
import com.example.githubapp.data.repository.User

private const val TAG = "GitApiController"

class GitApiController : IGitApiController {

    override suspend fun getRepositories(): List<Repository> {
        Log.d(TAG, "getRepositories: ")
        val response = GithubApiClient.apiService.getRepositories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch repositories: ${response.message()}")
        }
    }

    override suspend fun getRepositoryReadme(owner: String, repo: String, ref: String?): String {
        Log.d(TAG, "getRepositoryReadme: owner=$owner, repo=$repo, ref=$ref")
        val response = GithubApiClient.apiService.getRepositoryReadme(owner, repo, ref)
        if (response.isSuccessful) {
            val readme = response.body()
            return if (readme != null && readme.encoding == "base64") {
                String(android.util.Base64.decode(readme.content, android.util.Base64.DEFAULT))
            } else {
                readme?.content ?: ""
            }
        } else {
            throw Exception("Failed to fetch README: ${response.message()}")
        }
    }

    override suspend fun searchRepositories(
        query: String,
        sort: String?,
        order: String?
    ): List<Repository> {
        Log.d(TAG, "searchRepositories: query=$query, sort=$sort, order=$order")
        val response = GithubApiClient.apiService.searchRepositories(query, sort, order)
        if (response.isSuccessful) {
            return response.body()?.items ?: emptyList()
        } else {
            throw Exception("Failed to search repositories: ${response.message()}")
        }
    }

    override suspend fun getAuthenticatedUser(): User {
        Log.d(TAG, "getAuthenticatedUser: ")
        val response = GithubApiClient.apiService.getAuthenticatedUser()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("User data is null")
        } else {
            throw Exception("Failed to fetch user: ${response.message()}")
        }
    }
    
    override suspend fun getUserRepositories(): List<Repository> {
        Log.d(TAG, "getUserRepositories: ")
        val response = GithubApiClient.apiService.getUserRepositories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch user repositories: ${response.message()}")
        }
    }
}