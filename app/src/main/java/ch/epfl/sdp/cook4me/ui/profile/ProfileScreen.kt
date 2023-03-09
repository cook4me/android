package ch.epfl.sdp.cook4me.ui

import android.content.ClipData.Item
import android.media.Image
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.profile.PostGrid
import coil.compose.rememberAsyncImagePainter

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var bio by rememberSaveable { mutableStateOf("Hi there I'm a chef") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        ProfileImageAndUsername()


        //Textfield for the Favorite dish
        favoriteDish()

        //Textfield for the Allergies
        allergies()

        //Textfield for the bio
        bio()

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
            Image(painter = painter, contentDescription = "content")

        }
        username()
    }
}

@Composable
fun username() {
    var username by rememberSaveable { mutableStateOf("User 42") }

    Text(
        text = username,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun favoriteDish() {
    var favDish by rememberSaveable { mutableStateOf("Pizza") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Favorite dish", modifier = Modifier.width(100.dp))
        Text(text = "Pizza", modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun allergies() {
    var allergies by rememberSaveable { mutableStateOf("Hazelnut") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Allergies", modifier = Modifier.width(100.dp))
        Text(text = allergies, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun bio() {
    var bio by rememberSaveable { mutableStateOf("Hi there I'm a chef") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "Bio", modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp)
        )
        Text(
            text = bio, modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp)
        )
    }
}

