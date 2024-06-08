package com.example.c001apk.compose.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.c001apk.compose.c001Application

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
object NetWorkUtil {

    fun isWifiConnected(): Boolean {
        val cm = c001Application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = cm.activeNetwork
        if (network != null) {
            val nc = cm.getNetworkCapabilities(network)
            nc?.let {
                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return false
                }
            }
        }
        return false
    }

}