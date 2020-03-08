package com.andriiprudyus.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient

internal fun OkHttpClient.Builder.addInterceptors(
    interceptors: List<Interceptor>
): OkHttpClient.Builder {
    return this.apply {
        interceptors.forEach {
            addInterceptor(it)
        }
    }
}