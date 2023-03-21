import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Preview(showBackground = true)
@Composable
fun SignUpScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        saveCancelButtons()

        ProfileSetupImage_SignUpScreen()

        // Textfield for the email
        columnTextBtn_SignUpScreen(
            stringResource(R.string.tag_email),
            stringResource(R.string.default_email)
        )

        // Password field
        Password_signUpScreen()

        // Textfield for the username
        columnTextBtn_SignUpScreen(
            stringResource(R.string.tag_username),
            stringResource(R.string.default_username)
        )

        // Textfield for the Favorite dish
        columnTextBtn_SignUpScreen(
            stringResource(R.string.tag_favoriteDish),
            stringResource(R.string.default_favoriteDish)
        )

        // Textfield for the Allergies
        columnTextBtn_SignUpScreen(
            stringResource(R.string.tag_allergies),
            stringResource(R.string.default_allergies)
        )

        // Textfield for the bio
        bio_SignUpScreen()
    }
}

@Composable
fun bio_SignUpScreen() {
    var bio by rememberSaveable { mutableStateOf("") }
    input_row {
        Text(
            text = "Bio",
            modifier = Modifier
                .width(100.dp)
                .padding(top = 7.dp)
        )
        TextField(
            value = bio,
            onValueChange = { bio = it },
            placeholder = { Text(stringResource(R.string.default_bio)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.Black
            ),
            singleLine = false,
            modifier = Modifier
                .height(150.dp)
                .testTag(stringResource(R.string.tag_bio))
        )
    }
}

@Composable
fun Password_signUpScreen() {
    var password by rememberSaveable { mutableStateOf("") }

    input_row {
        Text(stringResource(R.string.tag_password), modifier = Modifier.width(100.dp))
        TextField(
            value = password,
            modifier = Modifier.testTag(stringResource(R.string.tag_password)),
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.default_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = colorsTextfield_SignUpScreen()
        )
    }
}

@Composable
fun columnTextBtn_SignUpScreen(
    displayLabel: String,
    defaultText: String
) {
    var textInputVariable by rememberSaveable {
        mutableStateOf("")
    }

    input_row {
        Text(
            text = displayLabel,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            placeholder = {
                Text(
                    defaultText
                )
            },
            value = textInputVariable,
            modifier = Modifier.testTag(displayLabel),
            onValueChange = { textInputVariable = it },
            colors = colorsTextfield_SignUpScreen()
        )
    }
}

@Composable
fun colorsTextfield_SignUpScreen(): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        textColor = Color.Black
    )

@Composable
fun ProfileSetupImage_SignUpScreen() {
    val imageURI = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageURI.value.isEmpty()) {
            R.drawable.ic_user
        } else {
            imageURI.value
        }
    )

    /**
     * TODO PUT INTO LOGIC
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

    Image_profileUpdateScreen(
        painter = painter,
        launcher = launcher
    )
}

@Composable
fun Image_SignUpScreen(
    painter: AsyncImagePainter,
    launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            ProfileUpdateImage_profileUpdateScreen(painter, launcher)
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun ProfileUpdateImage_SignUpScreen(
    painter: AsyncImagePainter,
    launcher: ManagedActivityResultLauncher<String, Uri?>
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

@Composable
private fun saveCancelButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text_buttons(nameBtn = stringResource(R.string.btn_cancel))

        text_buttons(nameBtn = stringResource(R.string.btn_save))
    }
}

@Composable
private fun text_buttons(nameBtn: String) {
    Text(
        text = nameBtn,
        modifier = Modifier
            .testTag(nameBtn)
            .clickable {}
    )
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
