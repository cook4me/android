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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.form.DatePicker
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.IntegerSlider
import ch.epfl.sdp.cook4me.ui.common.form.TimePicker
import ch.epfl.sdp.cook4me.ui.common.form.ToggleSwitch
import java.util.Calendar

/**
 * Component that shows the form to create an event
 */
@Composable
fun CreateEventScreen() {
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
        ToggleSwitch(
            question = stringResource(R.string.ask_event_visibility),
            answerChecked = stringResource(R.string.event_visibility_everyone),
            answerUnchecked = stringResource(R.string.event_visibility_subscriber_only),
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
                endMsg.value = if (event.value.isValidEvent) {
                    event.value.eventInformation
                } else {
                    event.value.eventProblem?.let { "Error: $it" } ?: "Error"
                }
            }
        )
        Text(text = endMsg.value)
    }
}
