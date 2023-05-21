package ch.epfl.sdp.cook4me.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

/**
 * This composable displays an indicator that the user is offline
 */
@Composable
fun OfflineStatusIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.SignalWifiOff,
            contentDescription = "Offline Icon",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(R.string.inform_offline_status),
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}