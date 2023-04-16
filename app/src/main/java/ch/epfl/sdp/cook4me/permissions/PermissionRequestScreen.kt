package ch.epfl.sdp.cook4me.permissions

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
// import androidx.compose.ui.tooling.preview.Preview

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

/*
@Preview
@Composable
fun PermissionRequestScreenPreview() {
    val permissionStatusProvider = ComposePermissionStatusProvider(
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    PermissionRequestScreen(permissionStatusProvider)
}
*/
