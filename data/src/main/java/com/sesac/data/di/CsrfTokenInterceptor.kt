package com.sesac.data.di

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CsrfTokenInterceptor @Inject constructor(
    private val cookieJar: CookieJar
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cookies = cookieJar.loadForRequest(request.url)
        val csrfToken = cookies.firstOrNull { it.name == "csrftoken" }?.value

        val newRequest = if (csrfToken != null) {
            request.newBuilder()
                .addHeader("X-CSRFToken", csrfToken)
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}
