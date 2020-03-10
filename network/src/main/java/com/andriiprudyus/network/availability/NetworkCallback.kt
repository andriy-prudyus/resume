package com.andriiprudyus.network.availability

import android.net.ConnectivityManager
import android.net.Network

class NetworkCallback : ConnectivityManager.NetworkCallback() {

    var isNetworkAvailable = false

    override fun onLost(network: Network) {
        super.onLost(network)
        isNetworkAvailable = false
    }

    override fun onUnavailable() {
        super.onUnavailable()
        isNetworkAvailable = false
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isNetworkAvailable = true
    }
}