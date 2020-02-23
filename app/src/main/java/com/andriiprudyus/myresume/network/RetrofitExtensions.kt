package com.andriiprudyus.myresume.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addInterceptors(interceptors: List<Interceptor>): OkHttpClient.Builder {
    return this.apply {
        interceptors.forEach {
            addInterceptor(it)
        }
    }
}