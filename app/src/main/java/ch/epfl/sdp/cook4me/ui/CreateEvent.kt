package ch.epfl.sdp.cook4me.ui

import java.util.Calendar
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
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.Event
import ch.epfl.sdp.cook4me.ui.simpleComponent.AddressInputReader
import ch.epfl.sdp.cook4me.ui.simpleComponent.DatePickerComponent
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputTextReader
import ch.epfl.sdp.cook4me.ui.simpleComponent.IntegerSlider
import ch.epfl.sdp.cook4me.ui.simpleComponent.TimePickerComponent
import ch.epfl.sdp.cook4me.ui.simpleComponent.ToggleButtonChoice


/**
 * Component that shows the form to create an event
 */
@Composable
fun CreateEvent() {
    val event = remember {
        mutableStateOf(Event())
    }
    val endMsg = remember { mutableStateOf("") }

    Column(
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(10.dp)
    ) {
        InputTextReader(question = "Name of the event?", onTextChanged = { event.value.name = it })
        InputTextReader(question = "Description of the event?", onTextChanged = { event.value.description = it })
        AddressInputReader(onAddressChanged = { event.value.location = it })
        IntegerSlider(text = "Number of people to invite", min = 2, max = 16, onValueChange = { event.value.maxParticipants = it }, modifier = Modifier.fillMaxWidth())
        ToggleButtonChoice(question = "Who can see the event", possibilities = Pair("Everyone", "Subscriber only"), onToggle = { event.value.isPrivate = it == "Subscriber only" })
        // submit button
        DatePickerComponent(initialDate = Calendar.getInstance(), onDateChange = { event.value.dateTime.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH)) })
        TimePickerComponent(
            onTimeChanged = {
                event.value.dateTime.set(Calendar.HOUR_OF_DAY, it.get(Calendar.HOUR_OF_DAY))
                event.value.dateTime.set(Calendar.MINUTE, it.get(Calendar.MINUTE))
            }
        )
        Button(
            onClick = {
                endMsg.value = if (event.value.isValidEvent()) {
                    event.value.showEventInformation()
                } else {
                    event.value.eventProblem()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Submit")
        }
        Text(text = endMsg.value)
    }
}
