package com.example.githubapp.data.repository

import android.util.Base64

object ReadmeDecoder {
    /**
     * 解码GitHub API返回的README内容
     * GitHub API返回的README内容是经过Base64编码的，需要解码才能正常使用
     *
     * @param encodedContent GitHub API返回的Base64编码的内容
     * @return 解码后的原始内容
     */
    fun decodeReadmeContent(encodedContent: String): String {
        return try {
            val decodedBytes = Base64.decode(encodedContent, Base64.DEFAULT)
            String(decodedBytes).trim()
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to decode README content"
        }
    }
}