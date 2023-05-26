package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ch.epfl.sdp.cook4me.ui.map.Locations.EPFL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private const val ZOOM_DEFAULT_VALUE = 15f

@Composable
fun LocationPicker(
    onLocationPicked: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(EPFL, ZOOM_DEFAULT_VALUE)
    }
    var markerState by remember {
        mutableStateOf<MarkerState?>(null)
    }
    var locationPicked by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(locationPicked) {
        locationPicked?.let {
            onLocationPicked(it)
            markerState = MarkerState(position = it)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, ZOOM_DEFAULT_VALUE),
                durationMs = 1000
            )
        }
    }

    Column {
        if (markerState == null) {
            Text("Click on the map to pick a location")
        }
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng -> locationPicked = latLng },
        ) {
            markerState?.let { state ->
                Marker(state = state)
            }
        }
    }
}
