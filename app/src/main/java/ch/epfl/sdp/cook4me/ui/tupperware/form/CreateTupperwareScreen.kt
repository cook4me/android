package ch.epfl.sdp.cook4me.ui.tupperware.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.ui.common.ImageSelector
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.FormState
import ch.epfl.sdp.cook4me.ui.common.form.ImageFieldState
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import kotlinx.coroutines.launch

@Composable
fun CreateTupperwareScreen(
    onCancel: () -> Unit,
    onSuccessfulSubmit: () -> Unit,
    repository: TupperwareRepository = TupperwareRepository()
) {
    val context = LocalContext.current
    val imageState by remember { mutableStateOf(ImageFieldState(context.getString(R.string.tupperware_field_image_error))) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                imageState.image = uri
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            imageUri?.let {
                if (success) {
                    imageState.image = it
                }
            }
        }
    )
    
    val scope = rememberCoroutineScope()

    fun onClickAddImage() {
        imagePicker.launch("image/*")
    }

    fun onClickTakePhoto() {
        val uri = ComposeFileProvider.getImageUri(context)
        imageUri = uri
        cameraLauncher.launch(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.create_tupper_screen_tag)),

    ) {
        TupperwareForm(
            modifier = Modifier,
            onClickAddImage = { onClickAddImage() },
            onClickTakePhoto = { onClickTakePhoto() },
            onImageClick = { imageState.image = null },
            imageState = imageState,
            onCancel = onCancel,
            onSubmit = { title, description ->
                scope.launch {
                    imageState.image?.let { repository.add(title, description, it) }
                    onSuccessfulSubmit()
                }
            }
        )
    }
}

@Composable
private fun TupperwareForm(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    onImageClick: () -> Unit = {},
    imageState: ImageFieldState,
    onCancel: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    println("TupperwareForm ${imageState.showErrors()}")
    val context = LocalContext.current
    val titleState = remember { RequiredTextFieldState(context.getString(R.string.tupperware_field_name_error)) }
    val descriptionState =
        remember { RequiredTextFieldState(context.getString(R.string.tupperware_field_description_error)) }
    val formState = FormState(
        titleState,
        descriptionState,
        imageState
    )

    var inProgress by remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            FormButtons(
                onCancelText = R.string.ButtonRowCancel,
                onSaveText = R.string.ButtonRowDone,
                onCancelClick = onCancel,
                isLoading = inProgress,
                onSaveClick = {
                    formState.enableShowErrors()
                    scope.launch {
                        if (formState.isValid) {
                            inProgress = true
                            onSubmit(titleState.text, descriptionState.text)
                        } else {
                            scaffoldState.snackbarHostState.showSnackbar(formState.getFirstError())
                        }
                    }
                }
            )
        }
    ) { it ->
        Box(
            modifier = modifier.padding(it)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Create a Tupperware",
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageSelector(
                        Modifier,
                        image = imageState.image,
                        onClickAddImage = onClickAddImage,
                        onClickTakePhoto = onClickTakePhoto,
                        onClickImage = onImageClick,
                        imageSize = 300.dp,
                        isError = imageState.showErrors()
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                InputField(
                    modifier = Modifier
                        .height(60.dp)
                        .onFocusChanged {
                            titleState.onFocusChange(it.isFocused)
                        }
                        .testTag("title")
                        .fillMaxWidth(),
                    question = R.string.TupCreateFormTupName,
                    value = titleState.text,
                    onValueChange = { titleState.text = it },
                    isError = titleState.showErrors(),
                )
                Spacer(modifier = Modifier.size(10.dp))
                InputField(
                    modifier = Modifier
                        .height(150.dp)
                        .onFocusChanged {
                            descriptionState.onFocusChange(it.isFocused)
                        }
                        .testTag("description")
                        .fillMaxWidth(),
                    question = R.string.TupCreateFormDesc,
                    value = descriptionState.text,
                    onValueChange = { descriptionState.text = it },
                    isError = descriptionState.showErrors(),
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}
