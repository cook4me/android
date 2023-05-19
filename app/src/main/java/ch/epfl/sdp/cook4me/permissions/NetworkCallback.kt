package ch.epfl.sdp.cook4me.permissions

import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.mutableStateOf

/**
 *  Network callback to check if the device is online
 *  Uses a mutable state to update the UI
 */
class NetworkCallback : ConnectivityManager.NetworkCallback() {
    val isOnline = mutableStateOf(false)

    override fun onAvailable(network: Network) {
        isOnline.value = true
    }

    override fun onLost(network: Network) {
        isOnline.value = false
    }
}
