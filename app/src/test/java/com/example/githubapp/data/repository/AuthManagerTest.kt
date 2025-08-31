package com.example.githubapp.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock

class AuthManagerTest {

    @Test
    fun test_isAccountCorrect(): Unit = runTest {
        val cacheManager = mock<AuthCacheManager>()
        val authManager = AuthManager.getInstance(cacheManager)

        val isCorrect = authManager.isAccountCorrect("username", "password")
        assert(!isCorrect)
    }
}