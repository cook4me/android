package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.epfl.sdp.cook4me.permissions.PermissionManager
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider

@Composable
fun MapPermissionWrapper(
    permissionStatusProvider: PermissionStatusProvider,
    modifier: Modifier,
    markers: List<MarkerData>,
    onCreateNewEventClick: () -> Unit = {},
    testing: Boolean = false
) {
    val permissionManager = PermissionManager(permissionStatusProvider)
    permissionManager.withPermission {
        GoogleMapView(
            modifier = modifier,
            markers = markers,
            onCreateNewEventClick = onCreateNewEventClick,
            userLocationDisplayed = !testing
        )
    }
}
