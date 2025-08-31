package com.example.githubapp.data.repository

import androidx.compose.runtime.mutableStateOf

/**
 * 认证管理器，用于管理用户登录状态
 */
class AuthManager private constructor(
    private val cacheManager: AuthCacheManager,
) {

    private val iToken = "35acf59ae8341b133c3a484c7990d1cf1ca0306e03191fe6ade30de4706e3f9c"

    // 使用MutableStateOf以便Compose可以监听状态变化
    val isLoggedIn = mutableStateOf(false)
    val authToken = mutableStateOf<String?>(null)

    init {
        // 初始化时检查是否有保存的token
        val token = cacheManager.getString("auth_token")
        isLoggedIn.value = !token.isNullOrEmpty()
        authToken.value = token
    }
    
    /**
     * 保存认证token
     */
    private suspend fun saveToken(token: String) {
        if (token.isEmpty()) {
            return
        }
        cacheManager.putString("auth_token", token, commit = true)
        authToken.value = token
        isLoggedIn.value = true
    }
    
    /**
     * 清除认证信息
     */
    fun clearAuth() {
        cacheManager.removeString("auth_token")
        authToken.value = null
        isLoggedIn.value = false
    }

    suspend fun isAccountCorrect(userName: String, password: String): Boolean {
        if (userName.isEmpty() || password.isEmpty()) {
            return false
        }
        val isCorrect = hash256(validP(password)) == iToken && userName == "github"
        if (isCorrect) {
            saveToken(validP(password))
        }
        return isCorrect
    }

    private fun validP(input: String): String {
        return "ghp_ig" + input + "9f47HjJV4AqJX8ENFrguKM2fwNg8"
    }

    private fun hash256(input: String): String {
        val bytes = input.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null
        
        fun getInstance(authCacheManager: AuthCacheManager): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(authCacheManager).also { INSTANCE = it }
            }
        }
    }
}