package com.example.githubapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AuthCacheManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("github_auth", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: AuthCacheManager? = null

        fun getInstance(context: Context): AuthCacheManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: AuthCacheManager(context.applicationContext).also {
                        INSTANCE = it
                    }
            }
        }
    }

    fun getString(key: String): String? {
        return prefs.getString(key, null)
    }

    fun putString(key: String, content: String, commit: Boolean = false) {
        prefs.edit(commit = commit) {
            putString(key, content)
        }
    }

    fun removeString(key: String) {
        prefs.edit { remove(key) }
    }
}