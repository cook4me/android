package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.epfl.sdp.cook4me.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val SATELLITE_LAT = 46.520544
    val SATELLITE_LON = 6.567825

    val satellite = LatLng(SATELLITE_LAT, SATELLITE_LON)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(satellite, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = satellite),
            title = "Satellite",
            snippet = "Marker in Satellite"
        )
    }
}
