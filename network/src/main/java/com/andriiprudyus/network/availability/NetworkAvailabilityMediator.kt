package com.andriiprudyus.network.availability

import android.net.ConnectivityManager
import android.net.NetworkRequest
import javax.inject.Inject

class NetworkAvailabilityMediator @Inject constructor(
    connectivityManager: ConnectivityManager,
    networkRequest: NetworkRequest,
    private val networkCallback: NetworkCallback
) {

    init {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    val isAvailable: Boolean
        get() = networkCallback.isNetworkAvailable
}