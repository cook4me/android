package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.eventform.Event
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapViewModel(
    eventService: EventFormService = EventFormService()
) : ViewModel() {
    private val _markers = mutableStateOf(emptyList<MarkerData>())
    val markers: State<List<MarkerData>> = _markers
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val events = eventService.retrieveAllEvents()
            val markers = eventsToMarkers(events)
            println(markers)
            _markers.value = markers
            _isLoading.value = false
        }
    }

    private fun eventsToMarkers(events: Map<String, Event>): List<MarkerData> =
        events.map { (id, event) ->
            MarkerData(
                position = LatLng(event.latLng.first, event.latLng.second),
                title = event.name,
                id = id,
                description = event.description
            )
        }
}
