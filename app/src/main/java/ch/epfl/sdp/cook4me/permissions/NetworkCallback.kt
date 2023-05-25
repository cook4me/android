package ch.epfl.sdp.cook4me.permissions

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.mutableStateOf

/**
 *  Network callback to check if the device is online
 *  Uses a mutable state to update the UI
 *  @param connectivityManager: The connectivity manager to check the network status
 */
class NetworkCallback(connectivityManager: ConnectivityManager) : ConnectivityManager.NetworkCallback() {

    val isOnline = mutableStateOf(true)

    init {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isOnlineValue = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        isOnline.value = isOnlineValue
    }

    override fun onAvailable(network: Network) {
        isOnline.value = true
    }

    override fun onLost(network: Network) {
        isOnline.value = false
    }
}
