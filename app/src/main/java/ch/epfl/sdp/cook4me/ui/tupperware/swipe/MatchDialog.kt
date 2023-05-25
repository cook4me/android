package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.BuildConfig
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.ui.chat.createChatWithPairs
import ch.epfl.sdp.cook4me.ui.chat.provideChatClient
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun MatchDialog(
    user: String,
    otherUser: String,
    context: Context,
    onDismissRequest: () -> Unit,
    profileImageRepository: ProfileImageRepository = ProfileImageRepository()
) {
    val coroutineScope = rememberCoroutineScope()
    val userProfileImage = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val otherUserProfileImage = remember { mutableStateOf<Uri>(Uri.EMPTY) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userProfileImage.value = profileImageRepository.getProfile(user)
            otherUserProfileImage.value = profileImageRepository.getProfile(otherUser)
        }
    }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.95f)
            .fillMaxHeight(fraction = 0.7f),
        onDismissRequest = onDismissRequest,
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(height = 30.dp))
                Text(
                    text = stringResource(id = R.string.match_dialogue_title),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(all = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 3f), // Takes up 3/5 of the Column height
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically // Center vertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userProfileImage.value)
                            .build(),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop, // Crop the image to fill its bounds
                        modifier = Modifier
                            .weight(weight = 3f) // Takes up 3/5 of the Row width
                            .aspectRatio(ratio = 1f) // Ensure the image width and height are the same
                            .padding(end = 8.dp) // Adds padding to separate images
                            .clip(CircleShape) // Creates a circular shape
                    )
                    Image(
                        painter = painterResource(R.drawable.cheers),
                        contentDescription = "Cheers icon",
                        modifier = Modifier.weight(weight = 1.5f) // Takes up 1/5 of the Row width
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(otherUserProfileImage.value)
                            .build(),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop, // Crop the image to fill its bounds
                        modifier = Modifier
                            .weight(weight = 3f) // Takes up 3/5 of the Row width
                            .aspectRatio(ratio = 1f) // Ensure the image width and height are the same
                            .padding(start = 8.dp) // Adds padding to separate images
                            .clip(CircleShape)
                    )
                }
                Spacer(Modifier.height(height = 16.dp)) // You can adjust the height as needed
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CircleButton(
                        onClick = {
                            createChatWithPairs(
                                userEmail = user,
                                targetEmail = otherUser,
                                client = provideChatClient(
                                    apiKey = BuildConfig.CHAT_API_KEY,
                                    context = context
                                ),
                                context = context
                            )
                            onDismissRequest()
                        },
                        icon = Icons.Rounded.Chat,
                        color = MaterialTheme.colors.primary
                    )
                    CircleButton(
                        onClick = { onDismissRequest() },
                        icon = Icons.Rounded.Cancel,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        },
        buttons = {}
    )
}
