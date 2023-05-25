import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton
import ch.epfl.sdp.cook4me.ui.common.form.NonRequiredTextFieldState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

const val PLACEHOLDER_URI = "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.ic_user

@Composable
fun AddProfileInfoScreen(
    onAddingSuccess: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
    accountService: AccountService = AccountService(),
    profileRepository: ProfileRepository = ProfileRepository(),
    profileImageRepository: ProfileImageRepository = ProfileImageRepository(),
) {
    val context = LocalContext.current
    val favoriteDishState = remember {
        NonRequiredTextFieldState("")
    }
    val allergiesState = remember {
        NonRequiredTextFieldState("")
    }
    val bioState = remember {
        NonRequiredTextFieldState("")
    }
    var userImage by remember {
        mutableStateOf<Uri>(
            // get Uri from R.drawable.ic_user
            Uri.parse(PLACEHOLDER_URI)
        )
    }

    var inProgress by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    userImage = uri
                }
            }
        )

    fun onClickAddImage() {
        imagePicker.launch("image/*")
    }

    Scaffold(
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = modifier
                    .testTag(stringResource(R.string.add_profile_info_screen_tag))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicToolbar(stringResource(R.string.Add_profile_infos_top_bar_message))

                ImageHolder_AddProfileInfoScreen(
                    onClickAddImage = { onClickAddImage() },
                    image = userImage,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    value = favoriteDishState.text,
                    isError = false,
                    placeholder = { Text(stringResource(id = R.string.tag_favoriteDish)) },
                    onValueChange = { favoriteDishState.text = it }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    value = allergiesState.text,
                    isError = false,
                    placeholder = { Text(stringResource(id = R.string.tag_allergies)) },
                    onValueChange = { allergiesState.text = it }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    value = bioState.text,
                    isError = false,
                    placeholder = { Text(stringResource(id = R.string.tag_bio)) },
                    onValueChange = { bioState.text = it }
                )

                LoadingButton(
                    R.string.add_profile_finish,
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .testTag(stringResource(id = R.string.add_profile_info_screen_tag)),
                    inProgress
                ) {
                    scope.launch {
                        inProgress = true
                        try {
                            accountService.getCurrentUser()?.email?.let {
                                profileRepository.add(
                                    Profile(
                                        it,
                                        allergiesState.text,
                                        bioState.text,
                                        favoriteDishState.text
                                    )
                                )
                                if (userImage.toString() != PLACEHOLDER_URI) {
                                    profileImageRepository.add(userImage)
                                }
                            }
                            onAddingSuccess()
                        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                            inProgress = false
                            Log.e("add profile infos", e.message, e)
                            scaffoldState
                                .snackbarHostState
                                .showSnackbar(context.getString(R.string.add_profile_infos_error))
                        }
                    }
                }
                Button(
                    onClick = onSkipClick,
                    colors =
                    ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = stringResource(R.string.add_profile_skip_step), fontSize = 16.sp)
                }
            }
        }
    )
}

@Composable
fun ImageHolder_AddProfileInfoScreen(
    onClickAddImage: () -> Unit,
    image: Uri,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image_AddProfileInfoScreen(
                onClickAddImage = onClickAddImage,
                image = image,
            )
        }
        Text(text = "Add profile picture")
    }
}

@Composable
fun Image_AddProfileInfoScreen(
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
private fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
