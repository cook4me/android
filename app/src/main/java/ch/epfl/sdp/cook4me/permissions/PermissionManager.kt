package ch.epfl.sdp.cook4me.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class PermissionManager(
    //private val permissions: List<String>,
    private val permissionStatusProvider: PermissionStatusProvider
    ) {



    @Composable
    fun withPermission(content: @Composable () -> Unit) {
        //val permissionStates = rememberMultiplePermissionsState(permissions = permissions)
        //handlePermissionStates(permissionStates, content)
        handlePermissionStates(content)
    }

    @Composable
    private fun handlePermissionStates(
        //permissionStates: MultiplePermissionsState,
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
                    getTextToShowGivenPermissions(
                        permissionStatusProvider.getRevokedPermissions(),
                        permissionStatusProvider.shouldShowRationale()
                        //permissionStates.revokedPermissions,
                        //permissionStates.shouldShowRationale
                    ) +
                "Give permission!"
                )

                Button(onClick = { permissionsRequested = true }) {
                    Text("Request permissions")
                }
            }


        }
    }



    @OptIn(ExperimentalPermissionsApi::class)
    private fun getTextToShowGivenPermissions(
        permissions: List<String>,
        shouldShowRationale: Boolean
    ): String {
        val revokedPermissionsSize = permissions.size
        if (revokedPermissionsSize == 0) return ""

        val textToShow = StringBuilder().apply {
            append("The ")
        }

        for (i in permissions.indices) {
            textToShow.append(permissions[i])
            when {
                revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                    textToShow.append(", and ")
                }
                i == revokedPermissionsSize - 1 -> {
                    textToShow.append(" ")
                }
                else -> {
                    textToShow.append(", ")
                }
            }
        }
        textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
        textToShow.append(
            if (shouldShowRationale) {
                " important. Please grant all of them for the app to function properly."
            } else {
                " denied. The app cannot function without them."
            }
        )
        return textToShow.toString()
    }
}
