import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
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
import ch.epfl.sdp.cook4me.ui.common.form.*
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun AddProfileInfoScreen(
    viewModel: SignUpViewModel = SignUpViewModel(),
    onSuccessfullSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val usernameState =
        remember { UserNameState(context.getString(R.string.invalid_username_message)) }
    val favoriteDishState= remember {
        nonRequiredTextFieldState("","")
    }
    val allergiesState= remember {
        nonRequiredTextFieldState("","")
    }
    val bioState= remember {
        nonRequiredTextFieldState("","")
    }

    val username by viewModel.username
    val favoriteDish by viewModel.favoriteDish
    val allergies by viewModel.allergies
    val bio by viewModel.bio
    val userImage by viewModel.userImage

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
                    viewModel.addUserImage(
                        uri
                    )
                }
            }
        )

    fun onClickAddImage() {
        imagePicker.launch("image/*")
    }

    BasicToolbar(stringResource(R.string.Add_profile_infos_top_bar_message))

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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageHolder_AddProfileInfoScreen(
                    onClickAddImage = { onClickAddImage() },
                    image = userImage,
                )

                // Textfield for the Favorite dish
                UserField(
                    usernameState.text,
                    usernameState.showErrors(),
                    {
                        usernameState.text = it
                        viewModel::addUsername
                    },
                    )

                ProfileInfosField(
                    icon = Icons.Filled.Info,
                    preview = stringResource(id = R.string.tag_favoriteDish),
                    value = favoriteDishState.text,
                    isError = false,
                    onNewValue ={
                        favoriteDishState.text = it
                        viewModel::addFavoriteDish
                    } )

                ProfileInfosField(
                    icon = Icons.Filled.Info,
                    preview = stringResource(id = R.string.tag_allergies),
                    value = allergiesState.text,
                    isError = false,
                    onNewValue ={
                        allergiesState.text = it
                        viewModel::addAllergies
                    } )

                BiosField(
                    icon = Icons.Filled.Info,
                    preview = stringResource(id = R.string.tag_bio),
                    value = bioState.text,
                    isError = false,
                    onNewValue ={
                        bioState.text = it
                        viewModel::addBio
                    } )

                LoadingButton(
                    R.string.btn_continue,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp),
                    inProgress
                ) {
                    usernameState.enableShowErrors()
                    scope.launch {
                        if (!usernameState.isValid) {
                            scaffoldState
                                .snackbarHostState
                                .showSnackbar(usernameState.errorMessage)
                        } else {
                            try {
                                inProgress = true
                                if(!viewModel.isValidUsername(username = username)){
                                    throw Exception("invalid Username")
                                }
                                if(viewModel.checkForm()){
                                    throw Exception("invalid form")
                                }
                                viewModel.onSubmit()
                                onSuccessfullSignUp()
                            } catch (e: Exception) {
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.Add_profile_infos_invalid_user))
                                Log.d(
                                    context.getString(R.string.Add_profile_infos_invalid_user),
                                    e.stackTraceToString()
                                )
                            }
                        }
                    }
                }
            }
        })
}

@Composable
fun bio_AddProfileInfoScreen(
    displayLabel: String,
    inputText: String,
    change: (String) -> Unit
) {
    input_row {
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
            colors = ColorsTextfield_AddProfileInfoScreen(),
            singleLine = false,
            modifier = Modifier
                .height(150.dp)
                .testTag(displayLabel)
        )
    }
}

@Composable
fun columnText_AddProfileInfoScreen(
    label: String,
    inputText: String,
    change: (String) -> Unit
) {
    input_row {
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
            colors = ColorsTextfield_AddProfileInfoScreen()
        )
    }
}

@Composable
fun ColorsTextfield_AddProfileInfoScreen(): TextFieldColors = TextFieldDefaults.textFieldColors(
    backgroundColor = Color.Transparent, textColor = Color.Black
)

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

@Composable
private fun text_buttons(onClick: () -> Unit, nameBtn: String) {
    Text(
        text = nameBtn,
        modifier = Modifier
            .testTag(nameBtn)
            .clickable(onClick = { onClick() })
    )
}

@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant

