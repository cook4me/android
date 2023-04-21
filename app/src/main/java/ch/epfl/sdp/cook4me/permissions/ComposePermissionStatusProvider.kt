package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class ComposePermissionStatusProvider(
    private val permissions: List<String>
) : PermissionStatusProvider {
    @Composable
    override fun allPermissionsGranted() = rememberMultiplePermissionsState(permissions).allPermissionsGranted

    @Composable
    override fun shouldShowRationale() = rememberMultiplePermissionsState(permissions).shouldShowRationale

    @Composable
    override fun getRevokedPermissions() = rememberMultiplePermissionsState(permissions)
        .revokedPermissions.map { it.permission }.toList()

    @Composable
    override fun RequestAllPermissions() {
        val permissionsState = rememberMultiplePermissionsState(permissions)
        LaunchedEffect(permissionsState) { permissionsState.launchMultiplePermissionRequest() }
    }
}
