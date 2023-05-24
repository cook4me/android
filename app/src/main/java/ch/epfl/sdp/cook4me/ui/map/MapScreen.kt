package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import ch.epfl.sdp.cook4me.ui.map.buttons.ButtonEPFL
import ch.epfl.sdp.cook4me.ui.map.buttons.ButtonUNIL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val ZOOM_DEFAULT_VALUE = 15f

data class MarkerData(
    val position: LatLng,
    val title: String,
    val id: String,
    val description: String
)

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    mapViewModel: MapViewModel = MapViewModel(),
    selectedEventId: String = "",
    userLocationDisplayed: Boolean = false,
    onCreateNewEventClick: () -> Unit = {},
    onDetailedEventClick: (String) -> Unit = {},
    isOnline: Boolean = true,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(Locations.EPFL, ZOOM_DEFAULT_VALUE)
    }

    val loadedMarkers by mapViewModel.markers
    var uniLoc by remember { mutableStateOf<LatLng?>(null) }

    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = userLocationDisplayed
            )
        )
    }

    // Add smooth animation from current position to target
    LaunchedEffect(uniLoc) {
        uniLoc?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, ZOOM_DEFAULT_VALUE),
                durationMs = 1000
            )
        }
    }

    val onClickUniversity = { uniLocation: LatLng ->
        uniLoc = uniLocation
    }
    var selectedMarker by remember { mutableStateOf(findMarkerById(loadedMarkers, selectedEventId)) }
    // var navigateToEvent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag(stringResource(R.string.event_screen_tag))
    ) {
        CreateNewItemButton(
            itemType = stringResource(R.string.event),
            onClick = onCreateNewEventClick,
            canClick = isOnline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties,
                    uiSettings = uiSettings,
                    onMapLoaded = {
                        onMapLoaded.invoke()
                    },
                ) {
                    loadedMarkers.map { marker ->
                        val markerState = rememberMarkerState(position = marker.position)
                        Marker(
                            state = markerState,
                            onClick = {
                                selectedMarker = marker
                                false
                            }
                        )
                    }
                    content()
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(250.dp)
                ) {
                    ButtonEPFL(
                        onClick = { onClickUniversity(Locations.EPFL) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ButtonUNIL(
                        onClick = { onClickUniversity(Locations.UNIL) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            if (selectedMarker != null) {
                selectedMarker?.let { marker ->
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = 4.dp,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = marker.title,
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.primary,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = marker.description,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSecondary,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    if (selectedMarker is MarkerData) {
                                        val eventId = (selectedMarker as MarkerData).id
                                        onDetailedEventClick(eventId)
                                    }
                                }
                            ) {
                                Text(
                                    text = "Explore event",
                                    style = MaterialTheme.typography.button,
                                    color = MaterialTheme.colors.onSecondary
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = 4.dp,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Select an event",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun findMarkerById(markers: List<MarkerData>, markerId: String): MarkerData? =
    markers.find { marker -> marker.id == markerId }
