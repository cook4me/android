package ch.epfl.sdp.cook4me.permissions

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun PermissionRequestScreen(permissionStatusProvider: PermissionStatusProvider) {
    val permissionManager = remember {
        PermissionManager(
            permissionStatusProvider
        )
    }
    permissionManager.withPermission {
        Text("Camera and location permission Granted")
    }
}
