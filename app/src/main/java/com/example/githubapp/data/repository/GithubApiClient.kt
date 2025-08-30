package com.example.githubapp.data.repository

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GithubApiClient {
    private const val BASE_URL = "https://api.github.com/"
    private const val TRENDING_API_URL = "https://ghapi.huchen.dev/"

    private var authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .build()
        chain.proceed(request)
    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        
    private var trendingRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TRENDING_API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var apiService: GithubApiService = retrofit.create(GithubApiService::class.java)

    fun setAuthToken(token: String) {
        authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        trendingRetrofit = Retrofit.Builder()
            .baseUrl(TRENDING_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(GithubApiService::class.java)
    }
    
    fun getTrendingApiService(): GithubApiService {
        return trendingRetrofit.create(GithubApiService::class.java)
    }
}