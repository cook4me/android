package ch.epfl.sdp.cook4me.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class PermissionManager(private val permissions: List<String>) {

    @Composable
    fun withPermission(content: @Composable () -> Unit) {
        val permissionStates = rememberMultiplePermissionsState(permissions = permissions)
        handlePermissionStates(permissionStates, content)
    }

    @Composable
    private fun handlePermissionStates(
        permissionStates: MultiplePermissionsState,
        content: @Composable () -> Unit
    ) {
        if (permissionStates.allPermissionsGranted) {
            content()
        } else {
            Column {
                Text(
                    getTextToShowGivenPermissions(
                        permissionStates.revokedPermissions,
                        permissionStates.shouldShowRationale
                    )
                )
                Button(onClick = { permissionStates.launchMultiplePermissionRequest() }) {
                    Text("Request permissions")
                }
            }


        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun getTextToShowGivenPermissions(
        permissions: List<PermissionState>,
        shouldShowRationale: Boolean
    ): String {
        val revokedPermissionsSize = permissions.size
        if (revokedPermissionsSize == 0) return ""

        val textToShow = StringBuilder().apply {
            append("The ")
        }

        for (i in permissions.indices) {
            textToShow.append(permissions[i].permission)
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
