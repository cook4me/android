package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class ComposePermissionStatusProvider(
    private val permissions: List<String>
):PermissionStatusProvider {
    @Composable
    private fun getPermissionsState(): MultiplePermissionsState {
        return rememberMultiplePermissionsState(permissions)
    }

    @Composable
    override fun allPermissionsGranted(): Boolean {
        val permissionsState = getPermissionsState()
        return permissionsState.allPermissionsGranted
    }

    @Composable
    override fun shouldShowRationale(): Boolean {
        val permissionsState = getPermissionsState()
        return permissionsState.shouldShowRationale
    }

    @Composable
    override fun getRevokedPermissions(): List<String> {
        val permissionsState = getPermissionsState()
        return permissionsState.revokedPermissions.map { it.permission }.toList()
    }

    @Composable
    override fun requestAllPermissions() {
        val permissionsState = getPermissionsState()
        LaunchedEffect(permissionsState) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
