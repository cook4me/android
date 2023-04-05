package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable


interface PermissionStatusProvider {

    @Composable fun allPermissionsGranted(): Boolean
    @Composable fun shouldShowRationale(): Boolean
    @Composable fun getRevokedPermissions(): List<String>
    @Composable fun requestAllPermissions()

}