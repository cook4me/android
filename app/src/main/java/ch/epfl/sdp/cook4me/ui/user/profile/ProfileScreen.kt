package ch.epfl.sdp.cook4me.ui.user.profile

import android.net.Uri
import android.net.Uri.EMPTY
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Preview(showBackground = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userId: String? = null,
    profileRepository: ProfileRepository = ProfileRepository(),
    profileImageRepository: ProfileImageRepository = ProfileImageRepository(),
    accountService: AccountService = AccountService(),
) {
    var profile by remember { mutableStateOf(Profile()) }
    var isLoading by remember { mutableStateOf(true) }
    var profileImage by remember { mutableStateOf(EMPTY) }

    LaunchedEffect(Unit) {
        var email: String? = null
        @Suppress("TooGenericExceptionCaught")
        try {
            email = userId ?: accountService.getCurrentUserWithEmail()
        } catch (e: Exception) {
            println("Error while getting current user: $e")
        }
        email?.let {
            @Suppress("TooGenericExceptionCaught")
            try {
                profile = profileRepository.getById(it) ?: Profile()
                profileImage = profileImageRepository.getProfile(it)
            } catch (e: Exception) {
                println("Error while getting profile: $e")
            }
            isLoading = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.profile_screen_tag))
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = {
                        Text(
                            profile.name.ifBlank { profile.email },
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                )
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(24.dp)
                        .size(250.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(profileImage)
                            .build(),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxHeight()
                            .testTag("ProfileImage")
                            .wrapContentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Row(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(Modifier.weight(weight = 0.3f)) {
                            if (profile.favoriteDish.isNotBlank()) {
                                Text(
                                    "Favorite Dish",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            if (profile.allergies.isNotBlank()) {
                                Text(
                                    "Allergies",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(weight = 0.05f))
                        Column(Modifier.weight(weight = 0.65f)) {
                            if (profile.favoriteDish.isNotBlank()) {
                                Text(profile.favoriteDish, modifier = Modifier.padding(top = 8.dp))
                            }
                            if (profile.allergies.isNotBlank()) {
                                Text(profile.allergies, modifier = Modifier.padding(top = 8.dp))
                            }
                        }
                    }
                    if (profile.bio.isNotBlank()) {
                        Column(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "About me",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(profile.bio, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
