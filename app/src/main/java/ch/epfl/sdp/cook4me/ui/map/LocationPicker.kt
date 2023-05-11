package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ch.epfl.sdp.cook4me.ui.map.Locations.EPFL
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val ZOOM_DEFAULT_VALUE = 15f

@Composable
fun LocationPicker(
    onLocationPicked: (LatLng) -> Unit
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var markers by remember { mutableStateOf<List<LatLng?>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(EPFL, ZOOM_DEFAULT_VALUE)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {latLng ->
            markerPosition = null
            onLocationPicked(latLng)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, ZOOM_DEFAULT_VALUE)
            markerPosition = latLng
        },
    ) {
        markerPosition?.let { position ->
            val markerState = rememberMarkerState(position = position)
            Marker(
                state = markerState,
                draggable = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationPickerPreview() {
    LocationPicker { latLng ->
        println("Location picked: $latLng")
    }
}