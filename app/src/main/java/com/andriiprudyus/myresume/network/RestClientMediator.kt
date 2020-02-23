package com.andriiprudyus.myresume.network

import android.content.Context
import com.andriiprudyus.myresume.BuildConfig
import com.andriiprudyus.myresume.log.AppLog
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClientMediator(context: Context) {

    companion object {
        private const val BASE_URL = "https://api.github.com"
        private const val CONNECTION_TIMEOUT = 60L // seconds
        private const val READ_TIMEOUT = 60L // seconds
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptors(listOfNotNull(
                        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                            override fun log(message: String) {
                                AppLog.d(message)
                            }
                        }).apply {
                            level = if (BuildConfig.isLoggingEnabled) {
                                HttpLoggingInterceptor.Level.BODY
                            } else {
                                HttpLoggingInterceptor.Level.BASIC
                            }
                        },
                        if (BuildConfig.DEBUG) OkHttpProfilerInterceptor() else null
                    )
                    )
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val companyApi: CompanyApi by lazy {
        retrofit.create(CompanyApi::class.java)
    }
}