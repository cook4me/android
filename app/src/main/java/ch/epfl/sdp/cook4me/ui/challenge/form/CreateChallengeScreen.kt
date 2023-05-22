package ch.epfl.sdp.cook4me.ui.challenge.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import ch.epfl.sdp.cook4me.ui.common.form.DatePicker
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.TimePicker
import ch.epfl.sdp.cook4me.ui.map.LocationPicker
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.runBlocking
import java.util.Calendar

/**
 * Component that shows the form to create an event
 * @param challengeFormService the service that will be used to create the challenge
 * @param accountService the service that will be used to get the current user
 * @param onCancelClick the function that will be called when the user clicks on the cancel button
 */

@Composable
fun CreateChallengeScreen(
    challengeFormService: ChallengeFormService = ChallengeFormService(),
    accountService: AccountService = AccountService(),
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    val challenge = remember {
        mutableStateOf(Challenge())
    }
    val endMsg = remember { mutableStateOf("") }

    fun updateDate(calendar: Calendar) = challenge.value.dateTime.set(
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun updateTime(calendar: Calendar) {
        challenge.value.dateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        challenge.value.dateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
    }

    val userEmail = accountService.getCurrentUserWithEmail()
    userEmail?.let { challenge.value = challenge.value.copy(creator = userEmail) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        InputField(
            question = R.string.ask_challenge_name,
            value = challenge.value.name,
            onValueChange = { challenge.value = challenge.value.copy(name = it) }
        )
        InputField(
            question = R.string.ask_challenge_description,
            value = challenge.value.description,
            onValueChange = { challenge.value = challenge.value.copy(description = it) }
        )
        CookingGenreDropdown(
            initialSelectedGenre = challenge.value.type,
            onSelectedGenreChanged = { challenge.value = challenge.value.copy(type = it) }
        )
        DatePicker(
            initialDate = Calendar.getInstance(),
            onDateChange = { updateDate(it) }
        )
        TimePicker(
            onTimeChanged = { updateTime(it) }
        )
        LocationPicker(
            onLocationPicked = {
                challenge.value = challenge.value.copy(latLng = GeoPoint(it.latitude, it.longitude))
            }
        )
        FormButtons(
            onCancelText = R.string.ButtonRowCancel,
            onSaveText = R.string.ButtonRowDone,
            onCancelClick = onCancelClick,
            onSaveClick = {
                // call suspend function
                runBlocking {
                    endMsg.value = challengeFormService.submitForm(challenge.value) ?: "Challenge created!"
                }
                onDoneClick()
            }
        )
        Text(text = endMsg.value)
    }
}

@Suppress("MagicNumber")
@Composable
fun CookingGenreDropdown(
    initialSelectedGenre: String,
    onSelectedGenreChanged: (String) -> Unit
) {
    val cookingGenres = listOf(
        "African",
        "American",
        "Asian",
        "British",
        "Cajun",
        "Caribbean",
        "Chinese",
        "Eastern European",
        "French",
        "German",
        "Greek",
        "Indian",
        "Irish",
        "Italian",
        "Japanese",
        "Korean",
        "Mediterranean",
        "Mexican",
        "Middle Eastern",
        "Nordic",
        "Southern",
        "Spanish",
        "Swiss",
        "Thai",
        "Vietnamese"
    )

    val selectedGenre = if (initialSelectedGenre in cookingGenres) initialSelectedGenre else cookingGenres.first()
    val selectedIndex = remember { mutableStateOf(cookingGenres.indexOf(selectedGenre)) }
    val expanded = remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.ask_challenge_type), modifier = Modifier.padding(end = 8.dp))
        Box {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                    .clickable { expanded.value = !expanded.value },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = cookingGenres[selectedIndex.value], modifier = Modifier.padding(end = 8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown arrow",
                    modifier = Modifier.rotate(if (expanded.value) 180f else 0f)
                )
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 8.dp)
            ) {
                cookingGenres.forEachIndexed { index, genre ->
                    DropdownMenuItem(
                        onClick = {
                            selectedIndex.value = index
                            expanded.value = false
                            onSelectedGenreChanged(genre)
                        }
                    ) {
                        Text(text = genre)
                    }
                }
            }
        }
    }
}
