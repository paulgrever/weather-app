package com.paul.weatherapp.data.api

import com.paul.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AppIdInterceptor : Interceptor {
    companion object {
        const val APP_ID = "appid"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()

        val urlWithApiKey = originalUrl.newBuilder()
            .addQueryParameter(APP_ID, BuildConfig.API_KEY)
            .build()

        // Build the new request with the updated URL
        val requestWithApiKey = originalRequest.newBuilder()
            .url(urlWithApiKey)
            .build()

        return chain.proceed(requestWithApiKey)
    }
}