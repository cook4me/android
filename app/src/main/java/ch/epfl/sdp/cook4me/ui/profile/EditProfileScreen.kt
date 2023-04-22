package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = remember{ProfileViewModel()},
) {
    val profile = viewModel.profileState.value
    val isLoading = viewModel.isLoading.value

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    viewModel.addUserImage(
                        uri
                    )
                }
            }
        )

    fun onClickAddImage() {
        imagePicker.launch("image/*")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center).testTag("CircularProgressIndicator")
            )
        } else {
            Column(
        modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .fillMaxHeight()
          ) {
            SaveCancelButtons(viewModel::onSubmit)
        ImageHolder_profileUpdateScreen(
            onClickAddImage = { onClickAddImage() },
            image = profile.userImage.toUri(),
        )

        // Textfield for the username
        ColumnTextBtnProfileUpdateScreen(
            stringResource(R.string.tag_username),
            profile.name,
            viewModel::addUsername,
        )

        // Textfield for the Favorite dish
        ColumnTextBtnProfileUpdateScreen(
            stringResource(R.string.tag_favoriteDish),
            profile.favoriteDish,
            viewModel::addFavoriteDish,
        )

        ColumnTextBtnProfileUpdateScreen(
            stringResource(R.string.tag_allergies),
            profile.allergies,
            viewModel::addAllergies,
        )

        // Textfield for the bio
        BioProfileUpdateScreen(
            stringResource(R.string.tag_bio),
            profile.bio,
            viewModel::addBio,
        )
    }
}}}

@Composable
fun BioProfileUpdateScreen(
    displayLabel: String,
    inputText: String,
    change: (String) -> Unit
) {
    InputRow {
        Text(
            text = displayLabel,
            modifier = Modifier
                .width(100.dp)
                .padding(top = 7.dp)
        )
        TextField(
            value = inputText,
            onValueChange = { change(it) },
            placeholder = { Text(stringResource(R.string.default_bio)) },
            colors = colorsTextfieldProfileUpdateScreen(),
            singleLine = false,
            modifier = Modifier
                .height(150.dp)
                .testTag(displayLabel)
        )
    }
}

@Composable
fun ColumnTextBtnProfileUpdateScreen(
    label: String,
    inputText: String,
    change: (String) -> Unit
) {
    InputRow {
        Text(
            text = label, modifier = Modifier.width(100.dp)
        )
        TextField(
            placeholder = {
                Text(
                    inputText
                )
            },
            value = inputText,
            modifier = Modifier.testTag(label),
            onValueChange = { change(it) },
            colors = colorsTextfieldProfileUpdateScreen()
        )
    }
}

@Composable
fun colorsTextfieldProfileUpdateScreen(): TextFieldColors = TextFieldDefaults.textFieldColors(
    backgroundColor = Color.Transparent, textColor = Color.Black
)

@Composable
fun ImageHolder_profileUpdateScreen(
    onClickAddImage: () -> Unit,
    image: Uri,
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
            Image_profileUpdateScreen(
                onClickAddImage = onClickAddImage,
                image = image,
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun Image_profileUpdateScreen(
    onClickAddImage: () -> Unit,
    image: Uri,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(image).build(),
        contentDescription = "",
        modifier = Modifier
            .fillMaxHeight()
            .testTag("ProfileImage")
            .wrapContentSize()
            .clickable { onClickAddImage() },
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun SaveCancelButtons(onSummit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButtons(onClick = {}, nameBtn = stringResource(R.string.btn_cancel))

        TextButtons(onClick = onSummit, nameBtn = stringResource(R.string.btn_save))
    }
}

@Composable
private fun InputRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun TextButtons(onClick: () -> Unit, nameBtn: String) {
    Text(
        text = nameBtn,
        modifier = Modifier
            .testTag(nameBtn)
            .clickable(onClick = { onClick() })
    )
}
