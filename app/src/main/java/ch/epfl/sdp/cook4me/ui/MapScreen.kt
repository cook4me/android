package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val satellite = LatLng(46.520544, 6.567825)
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
