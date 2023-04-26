package ch.epfl.sdp.cook4me.ui.tupperwareform

import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.permissions.PermissionManager
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider

@Composable
fun CreateTupperwarePermissionWrapper(
    permissionStatusProvider: PermissionStatusProvider,
) {
    val permissionManager = PermissionManager(permissionStatusProvider)
    permissionManager.WithPermission {
        CreateTupperwareScreen()
    }
}
