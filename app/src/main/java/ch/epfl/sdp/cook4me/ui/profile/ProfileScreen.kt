package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.profile.PostGrid
import coil.compose.rememberAsyncImagePainter

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .padding(8.dp)

    ) {
        ProfileImageAndUsername()

        //Textfield for the Favorite dish
        favoriteDish_profileScreen()

        //Textfield for the Allergies
        allergies_profileScreen()

        //Textfield for the bio
        bio_profileScreen()

        //Grid with post within
        PostGrid()
    }

}

@Composable
fun ProfileImageAndUsername() {
    //draws the image of the profile
    val imageURI = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageURI.value.isEmpty()) R.drawable.ic_user
        else imageURI.value
    )

    /**
     *Remembers and launches on recomposition
     *takes a contract and a on result function
     *contract = the action we want to take & Input/Output of the action
     *onResult = lambda that receives the result
     * launches an activity to get the image
     * the url received we places in imageURI.value
     * the painter will then get updated with the new value
     */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageURI.value = it.toString() }
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(painter = painter, contentDescription = "")

        }
        username_profileScreen()
    }
}

@Composable
fun username_profileScreen() {
    Text(
        text = stringResource(R.string.default_username),
        modifier = Modifier,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun favoriteDish_profileScreen() {
    var favDish by rememberSaveable { mutableStateOf(R.string.default_favoriteDish) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.tag_favoriteDish), modifier = Modifier.width(100.dp))
        Text(text = stringResource(favDish), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun allergies_profileScreen() {
    var allergies by rememberSaveable { mutableStateOf(R.string.default_allergies) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.tag_allergies), modifier = Modifier.width(100.dp))
        Text(text = stringResource(allergies), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun bio_profileScreen() {
    var bio by rememberSaveable { mutableStateOf(R.string.default_bio) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.tag_bio), modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp)
        )
        Text(
            text = stringResource(bio), modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

