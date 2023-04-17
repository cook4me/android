package ch.epfl.sdp.cook4me.ui.detailedevent

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.eventform.Event
import kotlinx.coroutines.launch

class DetailedEventViewModel(
    eventService: EventFormService = EventFormService(),
    accountService: AccountService = AccountService()
): ViewModel() {
    private val _firstEventState = mutableStateOf<Event>(Event())
    val firstEventState
        get()  = _firstEventState

    init {
        viewModelScope.launch {
            try {
                val userEmail = accountService.getCurrentUserEmail()
                userEmail?.let {
                    val firstEvent = eventService.getFirstEventWithGivenField("id", userEmail)
                    firstEvent?.let {
                        _firstEventState.value = it
                    }
                }
            } catch (e: NullPointerException) {
                Log.d("DetailedEventViewModel", "Current user is null! ${e.message}")
            }
        }
    }
}