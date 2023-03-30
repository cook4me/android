package ch.epfl.sdp.cook4me.permissions

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun dummyScreen() {
    val permissionManager = remember {
        PermissionManager(listOf(
                android.Manifest.permission.CAMERA
            )
        )
    }
    permissionManager.withPermission {
        Text("Camera and location permission Granted")
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    dummyScreen()
}