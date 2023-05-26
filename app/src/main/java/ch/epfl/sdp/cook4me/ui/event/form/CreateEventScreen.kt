package ch.epfl.sdp.cook4me.ui.event.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.common.form.DatePicker
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.FormTitle
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.MayParticipantsPicker
import ch.epfl.sdp.cook4me.ui.common.form.TimePicker
import ch.epfl.sdp.cook4me.ui.map.LocationPicker
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Component that shows the form to create an event
 * @param eventService the service that will be used to create the event
 * @param accountService the service that will be used to get the current user
 * @param onCancelClick the function that will be called when the user clicks on the cancel button
 */
@Composable
fun CreateEventScreen(
    eventService: EventFormService = EventFormService(),
    accountService: AccountService = AccountService(),
    onCancelClick: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = {},
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

    // for now I just set the id as the email of the current user for the sake of functionality
    val userEmail = accountService.getCurrentUserWithEmail()
    userEmail?.let { event.value = event.value.copy(id = userEmail, creator = userEmail) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.padding(10.dp),
        scaffoldState = scaffoldState,
        bottomBar = {
            FormButtons(
                onCancelText = R.string.ButtonRowCancel,
                onSaveText = R.string.ButtonRowDone,
                onCancelClick = onCancelClick,
                onSaveClick = {
                    // call suspend function
                    scope.launch {
                        endMsg.value = eventService.submitForm(event.value) ?: ""
                        if (endMsg.value.isNotBlank()) {
                            scaffoldState.snackbarHostState.showSnackbar(endMsg.value)
                        } else {
                            onSuccessfulSubmit()
                        }
                    }
                }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .testTag(stringResource(R.string.create_event_screen_tag))
        ) {
            FormTitle(modifier = Modifier.padding(top = 15.dp, bottom = 20.dp), title = "Create an Event")
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
            // AddressField(onAddressChanged = { event.value = event.value.copy(location = it) })
            MayParticipantsPicker(
                textRes = R.string.ask_event_number_participants,
                onValueChange = { event.value = event.value.copy(maxParticipants = it) },
                modifier = Modifier.fillMaxWidth()
            )
            DatePicker(
                initialDate = Calendar.getInstance(),
                onDateChange = { updateDate(it) }
            )
            TimePicker(
                onTimeChanged = { updateTime(it) }
            )

            LocationPicker(
                modifier = Modifier.height(400.dp),
                onLocationPicked = {
                    event.value = event.value.copy(latLng = GeoPoint(it.latitude, it.longitude))
                }
            )
        }
    }
}
