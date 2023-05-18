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
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton
import ch.epfl.sdp.cook4me.ui.common.form.BiosField
import ch.epfl.sdp.cook4me.ui.common.form.NonRequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.common.form.ProfileInfosField
import ch.epfl.sdp.cook4me.ui.common.form.UserField
import ch.epfl.sdp.cook4me.ui.common.form.UserNameState
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch

@Composable
fun AddProfileInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel,
    onSuccessfulSignUp: () -> Unit,
    onSignUpFailure: () -> Unit
) {
    val context = LocalContext.current
    val usernameState =
        remember { UserNameState(context.getString(R.string.invalid_username_message)) }
    val favoriteDishState = remember {
        NonRequiredTextFieldState("")
    }
    val allergiesState = remember {
        NonRequiredTextFieldState("")
    }
    val bioState = remember {
        NonRequiredTextFieldState("")
    }
    val userImage = remember {
        mutableStateOf<Uri>(
            // get Uri from R.drawable.ic_user
            Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
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
                    userImage.value = uri
                    viewModel.addProfileImage(uri)
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
                    image = userImage.value,
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

                LoadingButton(
                    R.string.btn_continue,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                        .testTag(stringResource(id = R.string.btn_continue)),
                    inProgress
                ) {
                    usernameState.enableShowErrors()
                    scope.launch {
                        if (!usernameState.isValid) {
                            scaffoldState.snackbarHostState.showSnackbar(usernameState.errorMessage)
                        } else {
                            try {
                                inProgress = true
                                viewModel.onSubmit(
                                    onSignUpSuccess = onSuccessfulSignUp,
                                    onSignUpFailure = onSignUpFailure,
                                )
                            } catch (e: FirebaseAuthException) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    context.getString(R.string.Add_profile_infos_invalid_user),
                                )
                                Log.d(
                                    context.getString(R.string.Add_profile_infos_invalid_user),
                                    e.stackTraceToString()
                                )
                            }
                        }
                    }
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
