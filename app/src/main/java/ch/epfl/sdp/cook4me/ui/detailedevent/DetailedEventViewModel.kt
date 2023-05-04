package ch.epfl.sdp.cook4me.ui.detailedevent

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.eventform.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val DELAY_TIME = 1000L
class DetailedEventViewModel(
    eventId: String,
    eventService: EventFormService = EventFormService(),
) : ViewModel() {
    private val _eventState = mutableStateOf(Event())
    val eventState: State<Event> = _eventState
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            // this delay somehow solves the detailed event keeps loading problem
            // I DONT KNOW WHY
            delay(DELAY_TIME)
            val eventQueried = eventService.getEventWithId(eventId)

            eventQueried?.let {
                withContext(Dispatchers.Main) {
                    _eventState.value = it
                    _isLoading.value = false
                }
            }
        }
    }
}
