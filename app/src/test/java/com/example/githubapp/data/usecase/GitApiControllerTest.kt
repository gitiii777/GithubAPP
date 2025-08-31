package com.example.githubapp.data.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Test

class GitApiControllerTest {

    private val gitApiController = GitApiController()

    @Test
    fun test_getRepositories_not_empty(): Unit = runTest {
        val repositories = gitApiController.getRepositories().also {
            println(it.size)
        }
        assert(repositories.isNotEmpty())
    }

    @Test
    fun test_getRepositoryReadme(): Unit = runTest {
        val readme = gitApiController.getRepositoryReadme("gitiii777", "GithubAPP").also {
            println(it)
        }
        assert(readme.content.isNotEmpty())
        assert(readme.encoding == "base64")
    }

    @Test
    fun test_searchRepositories(): Unit = runTest {
        val repositories = gitApiController.searchRepositories("kotlin").also {
            println(it.size)
        }
        assert(repositories.isNotEmpty())
    }

    @Test
    fun test_getUserRepositories(): Unit = runTest {
        val result = runCatching {
            gitApiController.getUserRepositories().also {
                println(it.size)
            }
        }
        // need token
        assert(result.isFailure)
    }

    @Test
    fun test_getRepositoryIssues(): Unit = runTest {
        val result = runCatching {
            gitApiController.getRepositoryIssues("gitiii777", "GithubAPP").also {
                println(it.size)
            }
        }
        assert(result.isSuccess)
    }
}