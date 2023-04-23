package ch.epfl.sdp.cook4me.ui.tupperwareform

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.InputField
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.imageSelection.ImageSelector
import kotlinx.coroutines.launch

@Composable
fun CreateTupperwareScreen(
    onCancel: () -> Unit,
    onSuccessfulSubmit: () -> Unit,
    repository: TupperwareRepository = TupperwareRepository()
) {
    val images = remember { mutableStateListOf<Uri>() }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                images.add(uri)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            imageUri?.let {
                if (success) {
                    images.add(it)
                }
            }
        }
    )

    val context = LocalContext.current
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
            .fillMaxSize(),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Header", style = MaterialTheme.typography.h5, color = Color.White)
        }
        TupperwareForm(
            modifier = Modifier
                .weight(1f),
            onClickAddImage = { onClickAddImage() },
            onClickTakePhoto = { onClickTakePhoto() },
            onClickImage = {},
            images,
            onCancel = onCancel,
            onSubmit = { title, description ->
                scope.launch {
//                    repository.add(Tupperware(title, description))
                    repository.addE(title, description, images)
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
    onClickImage: () -> Unit = {},
    images: List<Uri>,
    onCancel: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    val context = LocalContext.current
    val titleState = remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val descriptionState =
        remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val formIsValid = titleState.isValid && descriptionState.isValid

    var inProgress by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.TupCreateFormAddImage),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.size(10.dp))
            ImageSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClickAddImage = onClickAddImage,
                onClickTakePhoto = onClickTakePhoto,
                onClickImage = onClickImage,
                images = images
            )
            Spacer(modifier = Modifier.size(10.dp))
            CustomDivider()
            Spacer(modifier = Modifier.size(5.dp))
            InputField(
                modifier = Modifier
                    .height(50.dp)
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
            CustomDivider()
            Spacer(modifier = Modifier.size(2.dp))
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
    FormButtons(
        onCancelText = R.string.ButtonRowCancel,
        onSaveText = R.string.ButtonRowDone,
        onCancelClick = onCancel,
        isLoading = inProgress,
        onSaveClick = {
            titleState.enableShowErrors()
            descriptionState.enableShowErrors()
            if (formIsValid) {
                inProgress = true
                onSubmit(titleState.text, descriptionState.text)
            } else {
                // TODO: show snackbar
            }
        }
    )
}
