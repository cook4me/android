package ch.epfl.sdp.cook4me.ui.eventCreationForm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.Event
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.simpleComponent.DatePickerComponent
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputField
import ch.epfl.sdp.cook4me.ui.simpleComponent.IntegerSlider
import ch.epfl.sdp.cook4me.ui.simpleComponent.TimePickerComponent
import ch.epfl.sdp.cook4me.ui.simpleComponent.ToggleButtonChoice
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

/**
 * Component that shows the form to create an event
 * @param eventService the service that will be used to create the event
 */
@Composable
fun CreateEventScreen(
    eventService: EventFormService
) {
    val event = remember {
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

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        InputField(
            question = stringResource(R.string.ask_event_name),
            onTextChanged = { event.value = event.value.copy(name = it) }
        )
        InputField(
            question = stringResource(R.string.ask_event_description),
            onTextChanged = { event.value = event.value.copy(description = it) }
        )
        AddressField(onAddressChanged = { event.value = event.value.copy(location = it) })
        IntegerSlider(
            text = stringResource(R.string.ask_event_number_participants), min = 2, max = 16,
            onValueChange = { event.value = event.value.copy(maxParticipants = it) },
            modifier = Modifier.fillMaxWidth()
        )
        ToggleButtonChoice(
            question = stringResource(R.string.ask_event_visibility),
            answerChecked = stringResource(R.string.event_visibility_everyone),
            answerUnchecked = stringResource(R.string.event_visibility_subscriber_only),
            onToggle = {
                event.value = event.value.copy(isPrivate = it)
            }
        )
        DatePickerComponent(
            initialDate = Calendar.getInstance(),
            onDateChange = { updateDate(it) }
        )
        TimePickerComponent(
            onTimeChanged = { updateTime(it) }
        )
        Button(
            onClick = {
                runBlocking{
                    launch {
                        endMsg.value = eventService.submitForm(event.value) ?: ""
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.event_submit_button_test))
        }
        Text(text = endMsg.value)
    }
}
