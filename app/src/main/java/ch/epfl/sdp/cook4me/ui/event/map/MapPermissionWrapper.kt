package ch.epfl.sdp.cook4me.ui.event.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.epfl.sdp.cook4me.permissions.ComposePermissionStatusProvider
import ch.epfl.sdp.cook4me.permissions.PermissionManager
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider

@Composable
fun MapPermissionWrapper(
    permissionStatusProvider: PermissionStatusProvider,
    modifier: Modifier = Modifier,
    markers: List<MarkerData> = dummyMarkers,
    onCreateNewEventClick: () -> Unit = {},
    onDetailedEventClick: () -> Unit = {},
) {
    val permissionManager = PermissionManager(permissionStatusProvider)
    permissionManager.WithPermission {
        GoogleMapView(
            modifier = modifier.fillMaxSize(),
            markers = markers,
            onCreateNewEventClick = onCreateNewEventClick,
            userLocationDisplayed = permissionStatusProvider is ComposePermissionStatusProvider,
            onDetailedEventClick = onDetailedEventClick,
        )
    }
}
