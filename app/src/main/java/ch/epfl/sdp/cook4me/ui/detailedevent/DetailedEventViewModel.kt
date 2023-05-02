package ch.epfl.sdp.cook4me.ui.detailedevent

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.chat.provideChatClient
import ch.epfl.sdp.cook4me.ui.eventform.Event
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedEventViewModel(
    eventId: String,
    eventService: EventFormService = EventFormService(),
    context: Context,
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = context
    ),
) : ViewModel() {
    private val _eventState = mutableStateOf(Event())
    val eventState: State<Event> = _eventState
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
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
