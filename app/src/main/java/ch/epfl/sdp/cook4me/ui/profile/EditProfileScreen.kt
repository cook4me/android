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
import androidx.compose.runtime.mutableStateOf
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
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.ui.common.form.BiosField
import ch.epfl.sdp.cook4me.ui.common.form.NonRequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.common.form.ProfileInfosField
import ch.epfl.sdp.cook4me.ui.common.form.UserField
import ch.epfl.sdp.cook4me.ui.common.form.UserNameState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = remember { ProfileViewModel() },
    profileImageRepository: ProfileImageRepository = ProfileImageRepository(),
    onCancelListener: () -> Unit = {},
    onSuccessListener: () -> Unit = {},
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var username = viewModel.profile.value.name
    var bio = viewModel.profile.value.bio
    var favoriteDish = viewModel.profile.value.favoriteDish
    var allergies = viewModel.profile.value.allergies

    val usernameState =
        UserNameState(
            context.getString(R.string.invalid_username_message),
            username,
        )

    val allergiesState = NonRequiredTextFieldState(allergies)
    val bioState = NonRequiredTextFieldState(bio)
    val favoriteDishState = NonRequiredTextFieldState(favoriteDish)
    val isLoading = viewModel.isLoading.value
    val image = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var firstExecution = true

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    image.value = uri
                    viewModel.addProfileImage(uri)
                    // Save the image in the database
                    CoroutineScope(Dispatchers.Main).launch {
                        profileImageRepository.delete() // Delete the previous image
                        profileImageRepository.add(uri) // Add the new image
                    }
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
            // Way to force the values to be updated after loading the profile
            if (firstExecution) {
                allergiesState.text = allergies
                favoriteDishState.text = favoriteDish
                bioState.text = bio
                image.value = viewModel.profileImage.value
                firstExecution = false
            }

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
                            image = image.value,
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
    val context = LocalContext.current
    var userimage = image
    if (image == "".toUri()) {
        userimage = Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
    }

    AsyncImage(
        model = ImageRequest.Builder(context).data(userimage).build(),
        contentDescription = "",
        modifier = Modifier
            .fillMaxHeight()
            .testTag("ProfileImage")
            .wrapContentSize()
            .clickable { onClickAddImage() },
        contentScale = ContentScale.Crop,
        // Add key parameter to force recompose when image changes
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
