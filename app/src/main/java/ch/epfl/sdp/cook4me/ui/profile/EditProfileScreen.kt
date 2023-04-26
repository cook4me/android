package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.form.BiosField
import ch.epfl.sdp.cook4me.ui.common.form.NonRequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.common.form.ProfileInfosField
import ch.epfl.sdp.cook4me.ui.common.form.UserField
import ch.epfl.sdp.cook4me.ui.common.form.UserNameState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = remember { ProfileViewModel() },
    onCancelListener: () -> Unit = {},
    onSuccessListener: () -> Unit = {},
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val usernameState =
        remember {
            UserNameState(
                context.getString(R.string.invalid_username_message),
                viewModel.profileState.value.name
            )
        }
    val allergiesState = remember {
        NonRequiredTextFieldState("", viewModel.profileState.value.allergies)
    }
    val bioState = remember {
        NonRequiredTextFieldState("", viewModel.profileState.value.bio)
    }
    val favoriteDishState = remember {
        NonRequiredTextFieldState("", viewModel.profileState.value.favoriteDish)
    }

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
        modifier = modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .align(Alignment.Center)
                    .testTag("CircularProgressIndicator")
            )
        } else {
            Scaffold(
                scaffoldState = scaffoldState,
                content = { padding ->
                    Column(
                        modifier = modifier
                            .testTag(stringResource(R.string.Login_Screen_Tag))
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                            .padding(padding),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SaveCancelButtons(
                            { viewModel.onSubmit(onSuccessListener) },
                            onCancelListener,
                        )

                        ImageHolderProfileUpdateScreen(
                            onClickAddImage = { onClickAddImage() },
                            image = profile.userImage.toUri(),
                        )
                        // Textfield for the Username
                        UserField(
                            usernameState.text,
                            usernameState.showErrors(),
                            {
                                usernameState.text = it
                                viewModel.addUsername(it)
                            },
                        )

                        // Textfield for the Favorite dish
                        ProfileInfosField(
                            icon = Icons.Filled.Info,
                            preview = stringResource(id = R.string.tag_favoriteDish),
                            value = favoriteDishState.text,
                            isError = false,
                            onNewValue = {
                                favoriteDishState.text = it
                                viewModel.addFavoriteDish(it)
                            }
                        )

                        ProfileInfosField(
                            icon = Icons.Filled.Info,
                            preview = stringResource(id = R.string.tag_allergies),
                            value = allergiesState.text,
                            isError = false,
                            onNewValue = {
                                allergiesState.text = it
                                viewModel.addAllergies(it)
                            }
                        )

                        // Textfield for the bio
                        BiosField(
                            icon = Icons.Filled.Info,
                            preview = stringResource(id = R.string.tag_bio),
                            value = bioState.text,
                            isError = false,
                            onNewValue = {
                                bioState.text = it
                                viewModel.addBio(it)
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ImageHolderProfileUpdateScreen(
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
            ImageProfileUpdateScreen(
                onClickAddImage = onClickAddImage,
                image = image,
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun ImageProfileUpdateScreen(
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
private fun SaveCancelButtons(
    onSummit: () -> Unit,
    onCancelListener: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButtons(onClick = onCancelListener, nameBtn = stringResource(R.string.btn_cancel))

        TextButtons(onClick = onSummit, nameBtn = stringResource(R.string.btn_save))
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
