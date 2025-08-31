package com.example.githubapp.data.repository

import androidx.compose.runtime.mutableStateOf

/**
 * 认证管理器，用于管理用户登录状态
 */
class AuthManager private constructor(
    private val cacheManager: AuthCacheManager,
) {

    private val ik = "7ea16cbcbba2d67bd3cef7e592d53d453c0b57d2a568a248fd258b8b23019a7a"

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
        val isCorrect = hash256(validP(password)) == ik && userName == "github"
        if (isCorrect) {
            saveToken(validP(password))
        }
        return isCorrect
    }

    private fun validP(input: String): String {
        return "ghp_rU" + input + "bLjUKtybW0EHpKeGFKVXzx3cwtc3"
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