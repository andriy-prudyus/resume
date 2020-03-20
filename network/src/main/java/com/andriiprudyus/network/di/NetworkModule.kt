package com.andriiprudyus.network.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.andriiprudyus.network.CompanyService
import com.andriiprudyus.network.availability.NetworkAvailabilityMediator
import com.andriiprudyus.network.availability.NetworkCallback
import com.andriiprudyus.network.availability.NetworkInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(subcomponents = [NetworkComponent::class])
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://api.github.com"
        private const val CONNECTION_TIMEOUT = 60L // seconds
        private const val READ_TIMEOUT = 60L // seconds
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(networkAvailabilityMediator: NetworkAvailabilityMediator): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(NetworkInterceptor { networkAvailabilityMediator.isAvailable })
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCompanyRetrofitService(retrofit: Retrofit): CompanyService {
        return retrofit.create(CompanyService::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideNetworkRequest(): NetworkRequest = NetworkRequest.Builder().build()

    @Singleton
    @Provides
    fun provideNetworkCallback(): NetworkCallback = NetworkCallback()
}