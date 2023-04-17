package ch.epfl.sdp.cook4me.ui.eventform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.common.form.DatePicker
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.IntegerSlider
import ch.epfl.sdp.cook4me.ui.common.form.TimePicker
import ch.epfl.sdp.cook4me.ui.common.form.ToggleSwitch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

/**
 * Component that shows the form to create an event
 * @param eventService the service that will be used to create the event
 */
@Composable
fun CreateEventScreen(
    eventService: EventFormService = EventFormService(),
    accountService: AccountService = AccountService()
) {
    var event = remember {
        mutableStateOf(Event())
    }
    val endMsg = remember { mutableStateOf("") }

    fun updateDate(calendar: Calendar) = event.value.dateTime.set(
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun updateTime(calendar: Calendar) {
        event.value.dateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        event.value.dateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
    }

    // for now I just set the id as the email of the current user,
    // this needs to be changed!!!! because one user could have multiple events
    // we need to discuss a bit about it.
    val userEmail = accountService.getCurrentUserEmail()
    userEmail?.let { event.value = event.value.copy(id = userEmail) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        InputField(
            question = R.string.ask_event_name,
            value = event.value.name,
            onValueChange = { event.value = event.value.copy(name = it) }
        )
        InputField(
            question = R.string.ask_event_description,
            value = event.value.description,
            onValueChange = { event.value = event.value.copy(description = it) }
        )
        AddressField(onAddressChanged = { event.value = event.value.copy(location = it) })
        IntegerSlider(
            text = R.string.ask_event_number_participants, min = 2, max = 16,
            onValueChange = { event.value = event.value.copy(maxParticipants = it) },
            modifier = Modifier.fillMaxWidth()
        )
        ToggleSwitch(
            question = R.string.ask_event_visibility,
            answerChecked = R.string.event_visibility_everyone,
            answerUnchecked = R.string.event_visibility_subscriber_only,
            onToggle = {
                event.value = event.value.copy(isPrivate = it)
            }
        )
        DatePicker(
            initialDate = Calendar.getInstance(),
            onDateChange = { updateDate(it) }
        )
        TimePicker(
            onTimeChanged = { updateTime(it) }
        )

        FormButtons(
            onCancelText = R.string.ButtonRowCancel,
            onSaveText = R.string.ButtonRowDone,
            onCancelClick = { /*TODO*/ },
            onSaveClick = {
                // call suspend function
                runBlocking {
                    endMsg.value = eventService.submitForm(event.value) ?: "Event created!"
                }
            }
        )
        Text(text = endMsg.value)
    }
}
