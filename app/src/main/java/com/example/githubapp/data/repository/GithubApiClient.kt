package com.example.githubapp.data.repository

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "GithubApiClient"

object GithubApiClient {
    private const val BASE_URL = "https://api.github.com/"
    
    private lateinit var authManager: AuthManager
    
    fun initialize(context: Context) {
        authManager = AuthManager.getInstance(context)
    }
    
    
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = authManager.authToken.value

        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        chain.proceed(newRequest)
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .build()
    
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: GithubApiService = retrofit.create(GithubApiService::class.java)
}