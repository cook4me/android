package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import coil.compose.rememberAsyncImagePainter

@Preview(showBackground = true)
@Composable
fun ProfileUpdateScreen() {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        saveCancelButtons_profileUpdateScreen()

        ProfileSetupImage_profileUpdateScreen()

        //Textfield for the username
        username_profileUpdateScreen()
        //Textfield for the Favorite dish
        favoriteDish_profileUpdateScreen()

        //Textfield for the Allergies
        allergies_profileUpdateScreen()

        //Textfield for the bio
        bio_profileUpdateScreen()

    }
}

@Composable
fun bio_profileUpdateScreen() {

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
        TextField(
            value = bio,
            onValueChange = { bio = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            ),
            singleLine = false,
            modifier = Modifier.height(150.dp)
        )
    }
}

@Composable
fun allergies_profileUpdateScreen() {
    var allergies by rememberSaveable { mutableStateOf("No Allergies") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Allergies", modifier = Modifier.width(100.dp))
        TextField(
            value = allergies,
            onValueChange = { allergies = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )
    }
}

@Composable
fun favoriteDish_profileUpdateScreen() {
    var favDish by rememberSaveable { mutableStateOf("Pizza") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Favorite dish", modifier = Modifier.width(100.dp))
        TextField(
            value = favDish,
            onValueChange = { favDish = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )
    }
}

@Composable
fun ProfileSetupImage_profileUpdateScreen() {
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

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") }, //starts the launcher and accept all type of images
                contentScale = ContentScale.Crop //crops the image into the available space
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun saveCancelButtons_profileUpdateScreen() {
    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.btn_cancel), modifier = Modifier.clickable {
            notification.value = "cancelled"
        })
        Text(text = stringResource(R.string.btn_save), modifier = Modifier.clickable {
            notification.value = "Saved"
        })
    }
}

@Composable
fun username_profileUpdateScreen() {
    var username by rememberSaveable { mutableStateOf("User 42") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Username", modifier = Modifier.width(100.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )
    }
}



