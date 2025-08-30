package com.example.githubapp.data.usecase

import android.util.Log
import com.example.githubapp.data.repository.GithubApiClient
import com.example.githubapp.data.repository.Repository
import com.example.githubapp.data.repository.ReadmeDecoder

class GitApiController : IGitApiController {
    companion object {
        private const val TAG = "GitApiController"
    }

    override suspend fun getRepositories(): List<Repository> {
        return try {
            val response = GithubApiClient.apiService.getRepositories()
            if (response.isSuccessful) {
                response.body()?.also {
                    Log.d(TAG, "getRepositories: size: ${it?.size}")
                } ?: emptyList()
            } else {
                Log.e(TAG, "getRepositories: error response: ${response.code()} ${response.message()}")
                throw Exception("Failed to load repositories: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getRepositories: error: ", e)
            throw e
        }
    }
    
    /**
     * 获取指定仓库的README内容
     *
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param ref 分支、标签或commit SHA（可选，默认为默认分支）
     * @return 解码后的README内容
     */
    override suspend fun getRepositoryReadme(owner: String, repo: String, ref: String?): String {
        return try {
            val response = GithubApiClient.apiService.getRepositoryReadme(owner, repo, ref)
            if (response.isSuccessful) {
                val readme = response.body()
                if (readme != null) {
                    ReadmeDecoder.decodeReadmeContent(readme.content)
                } else {
                    "README not found"
                }
            } else {
                Log.e(TAG, "getRepositoryReadme: error response: ${response.code()} ${response.message()}")
                throw Exception("Failed to load README: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getRepositoryReadme: error: ", e)
            throw e
        }
    }
}