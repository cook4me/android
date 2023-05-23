package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import ch.epfl.sdp.cook4me.ui.map.buttons.ButtonEPFL
import ch.epfl.sdp.cook4me.ui.map.buttons.ButtonUNIL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val ZOOM_DEFAULT_VALUE = 15f
private const val MAP_SCREEN_PROPORTION = 0.8f

data class MarkerData(
    val position: LatLng,
    val title: String,
    val id: String,
    val description: String
)

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    mapViewModel: MapViewModel = MapViewModel(),
    selectedEventId: String = "",
    userLocationDisplayed: Boolean = false,
    onCreateNewEventClick: () -> Unit = {},
    onDetailedEventClick: (String) -> Unit = {},
    isOnline: Boolean = true,
) {
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
    var navigateToEvent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .testTag(stringResource(R.string.event_screen_tag))
    ) {
        CreateNewItemButton(
            itemType = stringResource(R.string.event),
            onClick = onCreateNewEventClick,
            canClick = isOnline
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1 - MAP_SCREEN_PROPORTION)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Row {
                selectedMarker?.let { marker ->
                    Column {
                        Text(
                            text = "Location: ${marker.title}",
                        )
                        if (navigateToEvent) {
                            Text(
                                text = "Navigate to event with id: ${marker.id}",
                            )
                        }
                    }
                    Button(
                        modifier = modifier.padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onPrimary,
                            contentColor = MaterialTheme.colors.primary
                        ),
                        onClick = {
                            navigateToEvent = !navigateToEvent
                            if (selectedMarker is MarkerData) {
                                val eventId = (selectedMarker as MarkerData).id
                                onDetailedEventClick(eventId)
                            }
                        }
                    ) {
                        Text(text = "Explore event", style = MaterialTheme.typography.body1)
                    }
                }
                Text(
                    text = "Select an event"
                )
            }
        }
        GoogleMap(
            modifier = modifier
                .fillMaxHeight(MAP_SCREEN_PROPORTION),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = {
                onMapLoaded.invoke()
            },
        ) {
            loadedMarkers.map { marker ->
                val markerState = rememberMarkerState(position = marker.position)
                MarkerInfoWindowContent(
                    state = markerState,
                    title = marker.title,
                    onClick = {
                        selectedMarker = marker
                        false
                    },
                    tag = marker.title,
                ) {
                    Text(marker.description)
                }
            }
            content()
        }
    }
}

private fun findMarkerById(markers: List<MarkerData>, markerId: String): MarkerData? =
    markers.find { marker -> marker.id == markerId }
