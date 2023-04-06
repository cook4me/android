package ch.epfl.sdp.cook4me.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class PermissionManager(
    private val permissionStatusProvider: PermissionStatusProvider
) {
    @Composable
    fun withPermission(content: @Composable () -> Unit) {
        handlePermissionStates(content)
    }

    @Composable
    private fun handlePermissionStates(
        content: @Composable () -> Unit
    ) {
        var permissionsRequested by remember { mutableStateOf(false) }
        if (permissionStatusProvider.allPermissionsGranted()) {
            content()
        } else {
            if (permissionsRequested) {
                permissionStatusProvider.requestAllPermissions()
            }
            Column {
                Text(
                    getPermissionText(
                        permissionStatusProvider.getRevokedPermissions(),
                        permissionStatusProvider.shouldShowRationale()
                    )
                )
                Button(onClick = { permissionsRequested = true }) {
                    Text("Request permissions")
                }
            }
        }
    }

    fun getPermissionText(permissions: List<String>, shouldShowRationale: Boolean): String {
        val permissionCount = permissions.size
        if (permissionCount == 0) return ""

        val permissionText = if (permissionCount == 1) "permission" else "permissions"

        val stringBuilder = StringBuilder("The ")

        for (i in permissions.indices) {
            stringBuilder.append(permissions[i])
            when {
                permissionCount > 1 && i == permissionCount - 2 -> stringBuilder.append(", and ")
                i == permissionCount - 1 -> stringBuilder.append(" ")
                else -> stringBuilder.append(", ")
            }
        }

        stringBuilder.append(" $permissionText ")
        stringBuilder.append(
            if (shouldShowRationale) {
                "is important. Please grant all of them for the app to function properly."
            } else {
                "are denied. The app cannot function without them."
            }
        )

        return stringBuilder.toString()
    }
}
