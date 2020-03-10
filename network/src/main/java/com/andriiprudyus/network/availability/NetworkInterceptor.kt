package com.andriiprudyus.network.availability

import com.andriiprudyus.network.exception.ErrorCode
import com.andriiprudyus.network.exception.NetworkException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class NetworkInterceptor(private val isNetworkAvailable: () -> Boolean) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NetworkException(ErrorCode.NO_INTERNET)
        }

        val response = chain.proceed(chain.request())

        if (response.isSuccessful) {
            return response
        }

        throw NetworkException(
            when (response.code) {
                HttpURLConnection.HTTP_NOT_FOUND -> ErrorCode.HTTP_NOT_FOUND
                HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorCode.HTTP_INTERNAL_ERROR
                else -> ErrorCode.UNKNOWN
            }
        )
    }
}