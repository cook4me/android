package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ch.epfl.sdp.cook4me.R
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = remember {
        ProfileViewModel()
    }
) {
    val profile = profileViewModel.profileState.value
    val userNameState = rememberSaveable { mutableStateOf("") }
    val isLoading = profileViewModel.isLoading.value
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .align(Alignment.Center)
                    .testTag("CircularProgressIndicator")
            )
        } else {
            userNameState.value = profile.name
            Column(
                modifier = modifier
                    .padding(12.dp)
                    .fillMaxHeight()
            ) {
                ProfileImageAndUsername(
                    profile.userImage.toUri(),
                    profile.name,
                    modifier
                )

                // Textfield for the Favorite dish
                FavoriteDishProfileScreen(
                    profile.favoriteDish,
                    modifier,
                )

                // Textfield for the Allergies
                AllergiesProfileScreen(
                    profile.allergies,
                    modifier,
                )

                // Textfield for the bio
                BioProfileScreen(
                    profile.bio,
                    modifier,
                )

                // Grid with post within
                PostGrid(modifier) // put images inside
            }
        }
    }
}

@Composable
fun ProfileImageAndUsername(userImage: Uri, name: String, modifier: Modifier) {
    // draws the image of the profile
    val imageURI = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (userImage.toString().isEmpty()) {
            R.drawable.ic_user
        } else {
            imageURI.value = userImage.toString()
        }
    )

    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            modifier = modifier
                .padding(8.dp)
                .size(100.dp)
                .testTag(stringResource(R.string.tag_defaultProfileImage))
        ) {
            Image(painter = painter, contentDescription = "")
        }

        UsernameProfileScreen(name, modifier)
    }
}

@Composable
fun UsernameProfileScreen(name: String, modifier: Modifier) {
    Text(
        text = name,
        modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun FavoriteDishProfileScreen(favoriteDish: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tag_favoriteDish),
            modifier = modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = favoriteDish,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun AllergiesProfileScreen(allergies: String, modifier: Modifier) {
    Row(
        modifier = modifier.
            fillMaxWidth().
            padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tag_allergies),
            modifier = modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = allergies,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun BioProfileScreen(bio: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.tag_bio),
            modifier = modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = bio,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
