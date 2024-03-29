package ch.epfl.sdp.cook4me.ui.event.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.BuildConfig
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.chat.createChatWithEmail
import ch.epfl.sdp.cook4me.ui.chat.provideChatClient
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen

/**
 * A component screen that displays details of an event
 */
@Composable
fun DetailedEventScreen(
    eventId: String,
    detailedEventViewModel: DetailedEventViewModel = DetailedEventViewModel(eventId),
) {
    val context = LocalContext.current
    val event = detailedEventViewModel.eventState.value
    val isLoading = detailedEventViewModel.isLoading.value
    Box {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(MaterialTheme.colors.background)
                    .testTag(stringResource(R.string.detailed_event_screen_tag))
            ) {
                SectionWithTitle(title = stringResource(R.string.event_name), content = event.name)
                Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

                SectionWithTitle(title = stringResource(R.string.event_description), content = event.description)
                Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

                SectionWithTitle(
                    title = stringResource(R.string.event_max_participants),
                    content = event.maxParticipants.toString()
                )
                Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

                SectionWithTitle(title = stringResource(R.string.event_creator), content = event.creator)
                Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

                SectionWithTitle(title = stringResource(R.string.event_time), content = event.eventDate)
                Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

                Button(
                    onClick = {
                        createChatWithEmail(
                            targetEmail = event.id,
                            client = provideChatClient(
                                apiKey = BuildConfig.CHAT_API_KEY,
                                context = context
                            ),
                            context = context
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.detailed_event_create_chat), fontSize = 16.sp)
                }
            }
        }
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
