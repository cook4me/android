package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.Event
import ch.epfl.sdp.cook4me.ui.simpleComponent.*
import java.util.*

/**
 * Component that shows the form to create an event
 */
@Composable
fun CreateEvent() {
    val event = Event()

    Column (
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        InputTextReader(question = "Name of the event?", onTextChanged = {event.name = it})
        AddressInputReader()
        IntegerSlider(text = "Number of people to invite", min = 2, max = 16, onValueChange = {event.maxParticipants=it}, modifier = Modifier.fillMaxWidth())
        ToggleButtonChoice(question = "Who can see the event", possibilities = Pair("Everyone","Subscriber only"), onToggle = {event.isPrivate = it == "Subscriber only"})
        // submit button
        DatePickerComponent(initialDate = Calendar.getInstance(), onDateSelected = {event.dateTime.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH))})
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Submit")
        }
    }
}