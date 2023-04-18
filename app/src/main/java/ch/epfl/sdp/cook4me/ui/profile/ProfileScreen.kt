package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(
    profileCreationViewModel: ProfileCreationViewModel = ProfileCreationViewModel()
) {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        ProfileImageAndUsername(
            profileCreationViewModel.userImage.value, profileCreationViewModel.username.value
        )

        // Textfield for the Favorite dish
        favoriteDish_profileScreen(profileCreationViewModel.favoriteDish.value)

        // Textfield for the Allergies
        allergies_profileScreen(profileCreationViewModel.allergies.value)

        // Textfield for the bio
        bio_profileScreen(profileCreationViewModel.bio.value)

        // Grid with post within
        PostGrid() // put images inside
    }
}

@Composable
fun ProfileImageAndUsername(userImage: Uri, name: String) {
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
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .testTag(stringResource(R.string.tag_defaultProfileImage))
        ) {
            Image(painter = painter, contentDescription = "")
        }

        username_profileScreen(name)
    }
}

@Composable
fun username_profileScreen(name: String) {
    var userName = name
    if (name.isEmpty()) {
        userName = stringResource(R.string.default_username)
    }

    Text(
        text = userName,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun favoriteDish_profileScreen(favoriteDish: String) {
    var favDish = favoriteDish
    if (favoriteDish.isEmpty()) {
        favDish = stringResource(R.string.default_favoriteDish)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tag_favoriteDish),
            modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = favDish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun allergies_profileScreen(allergiesIn: String) {
    var allergies = allergiesIn
    if (allergiesIn.isEmpty()) {
        allergies = stringResource(R.string.default_allergies)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tag_allergies),
            modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = allergies,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun bio_profileScreen(bioIn: String) {
    var bio = bioIn
    if (bioIn.isEmpty()) {
        bio = stringResource(R.string.default_bio)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.tag_bio),
            modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = bio,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
