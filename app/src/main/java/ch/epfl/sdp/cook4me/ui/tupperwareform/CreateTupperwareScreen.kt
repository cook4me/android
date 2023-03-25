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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun CreateTupperwareScreenWithState(
    viewModel: TupCreationViewModel = viewModel()
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.addImage(uri)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            imageUri?.let {
                if (success) {
                    viewModel.addImage(it)
                }
            }
        }
    )

    val context = LocalContext.current

    fun onClickAddImage() {
        imagePicker.launch("image/*")
    }

    fun onClickTakePhoto() {
        val uri = ComposeFileProvider.getImageUri(context)
        imageUri = uri
        cameraLauncher.launch(uri)
    }

    CreateTupperwareScreen(
        onClickAddImage = { onClickAddImage() },
        onClickTakePhoto = { onClickTakePhoto() },
        viewModel = viewModel
    )
}

@Composable
fun CreateTupperwareScreen(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    onClickImage: () -> Unit = {},
    viewModel: TupCreationViewModel = viewModel(),
) {
    Column(
        modifier = modifier
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
            onClickImage = onClickImage,
            viewModel = viewModel
        )
        FormButtons(
            onCancelText = stringResource(R.string.ButtonRowCancel),
            onSaveText = stringResource(R.string.ButtonRowDone),
            onCancelClick = { /*TODO*/ },
            onSaveClick = { viewModel.onSubmit() }
        )
    }
}

@Composable
fun TupperwareForm(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    onClickImage: () -> Unit = {},
    viewModel: TupCreationViewModel,
) {
    val titleText by viewModel.titleText
    val descText by viewModel.descText
    val formError by viewModel.formError

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            CustomTitleText(stringResource(R.string.TupCreateFormAddImage))

            Spacer(modifier = Modifier.size(10.dp))

            ImageSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClickAddImage = onClickAddImage,
                onClickTakePhoto = onClickTakePhoto,
                onClickImage = onClickImage,
                images = viewModel.images
            )
            Spacer(modifier = Modifier.size(10.dp))
            CustomDivider()
            Spacer(modifier = Modifier.size(5.dp))
            CustomTitleText(stringResource(R.string.TupCreateFormTupName))
            Spacer(modifier = Modifier.size(5.dp))
            CustomTextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                contentDescription = "TitleTextField",
                value = titleText, onValueChange = { viewModel.updateTitle(it) },
                isError = formError,
                singleLine = true,
            )
            Spacer(modifier = Modifier.size(10.dp))
            CustomDivider()
            Spacer(modifier = Modifier.size(2.dp))
            CustomTitleText(stringResource(R.string.TupCreateFormDesc))
            Spacer(modifier = Modifier.size(5.dp))
            CustomTextField(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentDescription = "DescriptionTextField",
                value = descText, onValueChange = { viewModel.updateDesc(it) },
                isError = formError,
            )
            Spacer(modifier = Modifier.size(10.dp))
            CustomDivider()
            Spacer(modifier = Modifier.size(2.dp))
            CustomTitleText(stringResource(R.string.TupCreateFormTags))
            Spacer(modifier = Modifier.size(5.dp))
            // TODO implement tags
            CustomTextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                value = "", onValueChange = {},
                contentDescription = "TagsTextField",
                isError = formError,
            )
        }
    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    contentDescription: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    isError: Boolean = false
) {
    val stateDescription = if (isError) {
        "Error"
    } else {
        ""
    }

    TextField(
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            this.stateDescription = stateDescription
        },
        textStyle = MaterialTheme.typography.caption,
        value = value, onValueChange = onValueChange,
        isError = isError,
        shape = RoundedCornerShape(30.dp),
        singleLine = singleLine,
        colors = textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
private fun CustomTitleText(text: String = "") {
    Text(
        modifier = Modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}

@Preview("default", showBackground = true)
@Composable
fun TupCreationScreenPreview() {
    Cook4meTheme {
        CreateTupperwareScreen(
            onClickTakePhoto = {},
            onClickAddImage = {},
        )
    }
}
