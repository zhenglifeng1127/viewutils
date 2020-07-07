package com.ocsa.carshop.net.base

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenHeadInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val token = ""
        val request: Request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization", "Bearer $token"
            )
            .build()
        return chain.proceed(request)
    }
}