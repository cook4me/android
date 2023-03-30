package ch.epfl.sdp.cook4me.ui.eventform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

/**
 * A component screen that displays details of an event
 * @param event the event to be displayed
 */
@Composable
fun DetailedEventScreen(event: Event) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colors.background)
    ) {
        SectionWithTitle(title = stringResource(R.string.event_name), content = event.name)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_description), content = event.description)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_location), content = event.location)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(
            title = stringResource(R.string.event_max_participants),
            content = event.maxParticipants.toString()
        )
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_creator), content = event.creator)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(
            title = stringResource(R.string.event_participants),
            content = event.participants.joinToString(separator = ", ")
        )
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        val accessibility = if (event.isPrivate) "Only Subscribers" else "Everyone"
        SectionWithTitle(title = stringResource(R.string.event_who_can_see_event), content = accessibility)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_time), content = event.eventDate)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)
    }
}

@Composable
fun SectionWithTitle(title: String, content: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )

        Text(
            text = content,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}
