package com.example.githubapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit

/**
 * 认证管理器，用于管理用户登录状态
 */
class AuthManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("github_auth", Context.MODE_PRIVATE)
    private val iToken = "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"

    // 使用MutableStateOf以便Compose可以监听状态变化
    val isLoggedIn = mutableStateOf(false)
    val authToken = mutableStateOf<String?>(null)

    init {
        // 初始化时检查是否有保存的token
        val token = prefs.getString("auth_token", null)
        isLoggedIn.value = !token.isNullOrEmpty()
        authToken.value = token
    }
    
    /**
     * 保存认证token
     */
    suspend fun saveToken(token: String = "") {
        val saveToken = token.ifEmpty { iToken }
        prefs.edit(commit = true) { putString("auth_token", saveToken) }
        authToken.value = saveToken
        isLoggedIn.value = true
    }
    
    /**
     * 清除认证信息
     */
    fun clearAuth() {
        prefs.edit() { remove("auth_token") }
        authToken.value = null
        isLoggedIn.value = false
    }
    
    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null
        
        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}