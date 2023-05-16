package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    onLocationPicked: (LatLng) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(EPFL, ZOOM_DEFAULT_VALUE)
    }
    var markerState by remember { mutableStateOf<MarkerState?>(null) }
    Column {
        if (markerState == null) {
            Text("Click on the map to pick a location")
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                onLocationPicked(latLng)
                markerState = MarkerState(position = latLng)
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT_VALUE))
            },
        ) {
            markerState?.let { state ->
                Marker(state = state)
            }
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
