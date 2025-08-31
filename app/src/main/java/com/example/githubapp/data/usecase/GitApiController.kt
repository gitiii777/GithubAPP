package com.example.githubapp.data.usecase

import com.example.githubapp.data.repository.GithubApiClient
import com.example.githubapp.data.repository.Issue
import com.example.githubapp.data.repository.IssueRequest
import com.example.githubapp.data.repository.ReadmeData
import com.example.githubapp.data.repository.Repository
import com.example.githubapp.data.repository.User

private const val TAG = "GitApiController"

class GitApiController : IGitApiController {

    override suspend fun getRepositories(): List<Repository> {
//        Log.d(TAG, "getRepositories: ")
        val response = GithubApiClient.apiService.getRepositories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch repositories: ${response.message()}")
        }
    }

    override suspend fun getRepositoryReadme(owner: String, repo: String, ref: String?): ReadmeData {
//        Log.d(TAG, "getRepositoryReadme: owner=$owner, repo=$repo, ref=$ref")
        val response = GithubApiClient.apiService.getRepositoryReadme(owner, repo, ref)
        if (response.isSuccessful) {
            val readme = response.body()
            return ReadmeData(readme?.content ?: "", readme?.encoding ?: "")
        } else {
            throw Exception("Failed to fetch README: ${response.message()}")
        }
    }

    override suspend fun searchRepositories(
        query: String,
        sort: String?,
        order: String?
    ): List<Repository> {
//        Log.d(TAG, "searchRepositories: query=$query, sort=$sort, order=$order")
        val response = GithubApiClient.apiService.searchRepositories(query, sort, order)
        if (response.isSuccessful) {
            return response.body()?.items ?: emptyList()
        } else {
            throw Exception("Failed to search repositories: ${response.message()}")
        }
    }

    override suspend fun getAuthenticatedUser(): User {
//        Log.d(TAG, "getAuthenticatedUser: ")
        val response = GithubApiClient.apiService.getAuthenticatedUser()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("User data is null")
        } else {
            throw Exception("Failed to fetch user: ${response.message()}")
        }
    }
    
    override suspend fun getUserRepositories(): List<Repository> {
//        Log.d(TAG, "getUserRepositories: ")
        val response = GithubApiClient.apiService.getUserRepositories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch user repositories: ${response.message()}")
        }
    }
    
    override suspend fun createIssue(owner: String, repo: String, issueRequest: IssueRequest): Issue {
//        Log.d(TAG, "createIssue: owner=$owner, repo=$repo, title=${issueRequest.title}")
        val response = GithubApiClient.apiService.createIssue(owner, repo, issueRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Issue data is null")
        } else {
            throw Exception("Failed to create issue: ${response.message()}")
        }
    }
    
    override suspend fun getRepositoryIssues(owner: String, repo: String, state: String?): List<Issue> {
//        Log.d(TAG, "getRepositoryIssues: owner=$owner, repo=$repo, state=$state")
        val response = GithubApiClient.apiService.getRepositoryIssues(owner, repo, state)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch repository issues: ${response.message()}")
        }
    }
}