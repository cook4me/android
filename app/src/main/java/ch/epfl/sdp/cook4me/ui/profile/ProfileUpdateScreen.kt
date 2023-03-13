import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import coil.compose.AsyncImagePainter
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

        // Textfield for the username
        username_profileUpdateScreen()
        // Textfield for the Favorite dish
        favoriteDish_profileUpdateScreen()

        // Textfield for the Allergies
        allergies_profileUpdateScreen()

        // Textfield for the bio
        bio_profileUpdateScreen()
    }
}

@Composable
fun bio_profileUpdateScreen() {
//    var bioText = stringResource(R.string.default_bio)
    var bio by rememberSaveable { mutableStateOf("") }
    input_row {
        Text(
            text = "Bio", modifier = Modifier
                .width(100.dp)
                .padding(top = 7.dp)
        )
        TextField(value = bio,
            onValueChange = { bio = it },
            placeholder = { Text(stringResource(R.string.default_bio)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            ),
            singleLine = false,
            modifier = Modifier
                .height(150.dp)
                .testTag(stringResource(R.string.tag_bio))
        )
    }
}

@Composable
fun allergies_profileUpdateScreen() {
//    var allergy = stringResource(R.string.default_allergies)
    var allergies by rememberSaveable {
        mutableStateOf("")
    }

    input_row {
        Text(text = "Allergies", modifier = Modifier.width(100.dp))
        TextField(
            value = allergies,
            placeholder = { Text(stringResource(R.string.default_allergies)) },
            modifier = Modifier.testTag(stringResource(R.string.tag_allergies)),
            onValueChange = { allergies = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )
    }
}

@Composable
fun favoriteDish_profileUpdateScreen() {
//    var favDishText = stringResource(R.string.default_favoriteDish)
    var favDish by rememberSaveable {
        mutableStateOf("")
    }

    input_row {
        Text(text = "Favorite dish", modifier = Modifier.width(100.dp))
        TextField(
            placeholder = {
                Text(
                    stringResource(
                        R.string.default_favoriteDish
                    )
                )
            },
            value = favDish,
            modifier = Modifier.testTag(stringResource(R.string.tag_favoriteDish)),
            onValueChange = { favDish = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )
    }
}

@Composable
fun ProfileSetupImage_profileUpdateScreen() {
    /**
     * TODO Put into logic
     */
    // draws the image of the profile
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

    Image_profileUpdateScreen(painter = painter, launcher = launcher)
}

@Composable
fun Image_profileUpdateScreen(
    painter: AsyncImagePainter, launcher: ManagedActivityResultLauncher<String, Uri?>
) {

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
                    .testTag("ProfileImage") // TODO
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") }, // starts the launcher and accept all type of images
                contentScale = ContentScale.Crop // crops the image into the available space
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun saveCancelButtons_profileUpdateScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.btn_cancel), modifier = Modifier
            .testTag(
                stringResource(
                    R.string.btn_save
                )
            )
            .clickable {})
        Text(text = stringResource(R.string.btn_save),
            modifier = Modifier
                .testTag(stringResource(R.string.btn_cancel))
                .clickable {})
    }
}

@Composable
fun username_profileUpdateScreen() {
    var username by rememberSaveable { mutableStateOf("") }

    input_row {
        Text(text = "Username", modifier = Modifier.width(100.dp))
        TextField(
            value = username,
            modifier = Modifier.testTag(stringResource(R.string.tag_username)),
            placeholder = { Text(stringResource(R.string.default_username)) },
            onValueChange = { username = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, textColor = Color.Black
            )
        )J
    }
}

@Composable
private fun input_row(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
