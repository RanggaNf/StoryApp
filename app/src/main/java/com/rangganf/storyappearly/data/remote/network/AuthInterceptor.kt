package com.rangganf.storyappearly.data.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        // header Authorization jika tidak ada header No-Authentication dan token tidak kosong
        val authenticatedRequest = if (request.header("No-Authentication") == null && token.isNotEmpty()) {
            val finalToken = "Bearer $token"
            request.newBuilder()
                .addHeader("Authorization", finalToken)
                .build()
        } else {
            // request asli jika tidak memenuhi kondisi di atas
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
