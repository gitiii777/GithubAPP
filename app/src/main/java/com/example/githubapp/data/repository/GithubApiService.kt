package com.example.githubapp.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("repositories")
    suspend fun getRepositories(
        @Query("since") since: Int = 0,
        @Query("per_page") perPage: Int = 30
    ): Response<List<Repository>>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<SearchResult>

    @GET("user")
    suspend fun getAuthenticatedUser(): Response<User>
    
    @GET("https://ghapi.huchen.dev/repositories")
    suspend fun getTrendingRepositories(
        @Query("language") language: String? = null,
        @Query("since") since: String? = "daily"
    ): Response<List<TrendingRepository>>
    
    @GET("repos/{owner}/{repo}/readme")
    suspend fun getRepositoryReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") ref: String? = null
    ): Response<RepositoryReadme>
}

data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val html_url: String,
    val stargazers_count: Int,
    val language: String?,
    val owner: Owner
)

data class Owner(
    val login: String,
    val avatar_url: String
)

data class SearchResult(
    val total_count: Int,
    val items: List<Repository>
)

data class User(
    val id: Long,
    val login: String,
    val name: String?,
    val email: String?,
    val avatar_url: String,
    val bio: String?,
    val public_repos: Int,
    val followers: Int,
    val following: Int
)

data class TrendingRepository(
    val author: String,
    val name: String,
    val avatar: String,
    val url: String,
    val description: String?,
    val language: String?,
    val languageColor: String?,
    val stars: Int,
    val forks: Int,
    val currentPeriodStars: Int,
    val builtBy: List<Contributor>?
)

data class Contributor(
    val username: String,
    val href: String?,
    val avatar: String
)

data class RepositoryReadme(
    val type: String,
    val encoding: String,
    val size: Int,
    val name: String,
    val path: String,
    val content: String,
    val sha: String,
    val url: String,
    val git_url: String,
    val html_url: String,
    val download_url: String?
)