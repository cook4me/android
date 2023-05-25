package ch.epfl.sdp.cook4me.ui.user.profile

import android.net.Uri
import android.net.Uri.EMPTY
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import coil.compose.rememberAsyncImagePainter

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
    var image by remember { mutableStateOf(EMPTY) }

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
                image = profileImageRepository.getProfile(it)
            } catch (e: Exception) {
                println("Error while getting profile: $e")
            }
            isLoading = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.profile_screen_tag)),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImageAndUsername(
                    image,
                    profile.name,
                    modifier,
                )

                // Textfield for the Favorite dish
                FavoriteDishRow(
                    profile.favoriteDish,
                    modifier,
                )

                // Textfield for the Allergies
                AllergiesRow(
                    profile.allergies,
                    modifier,
                )

                // Textfield for the bio
                BioRow(
                    profile.bio,
                    modifier,
                )
            }
        }
    }
}

@Composable
fun ProfileImageAndUsername(userImage: Uri, name: String, modifier: Modifier) {
    // draws the image of the profile
    val painter = rememberAsyncImagePainter(
        if (userImage.toString().isEmpty()) {
            R.drawable.ic_user
        } else {
            userImage
        }
    )

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
        )
        Card(
            shape = CircleShape,
            modifier = modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .size(200.dp)
                .testTag(stringResource(R.string.tag_defaultProfileImage))
        ) {
            Image(painter = painter, contentDescription = "")
        }
    }
}

@Composable
fun FavoriteDishRow(favoriteDish: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.tag_favoriteDish),
            modifier = modifier
                .width(120.dp)
                .padding(top = 8.dp, bottom = 8.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "\t\t$favoriteDish",
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun AllergiesRow(allergies: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.tag_allergies),
            modifier = modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "\t\t$allergies",
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun BioRow(bio: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.tag_bio),
            modifier = modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "\t\t$bio",
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
