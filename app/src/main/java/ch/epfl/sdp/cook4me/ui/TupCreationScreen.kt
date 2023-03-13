package ch.epfl.sdp.cook4me.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import java.io.File
class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}

@Composable
fun TupCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: TupCreationViewModel = viewModel(),
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if(uri != null) {
                viewModel.addImage(uri)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            imageUri?.let {
                if(success) {
                    viewModel.addImage(imageUri!!)
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
            Text(text="Header", style = MaterialTheme.typography.h5, color = Color.White)
        }
        TupperwareForm(
            modifier = Modifier
                .weight(1f),
            onClickAddImage = { onClickAddImage() },
            onClickTakePhoto = { onClickTakePhoto() },
            viewModel = viewModel
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            ButtonRow(
                modifier = Modifier.fillMaxSize(),
                onCancelPressed = {},
                onDonePressed={viewModel.onSubmit()},
            )
        }

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


    Box(
        modifier =  modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            FieldText(stringResource(R.string.TupCreateFormAddImage))

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
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(5.dp))
            FieldText(stringResource(R.string.TupCreateFormTupName))
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .testTag("TitleTextField"),
                textStyle = MaterialTheme.typography.caption,
                value = titleText, onValueChange = { viewModel.updateTitle(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.size(10.dp))
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(2.dp))
            FieldText(stringResource(R.string.TupCreateFormDesc))
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .testTag("DescriptionTextField"),
                textStyle = MaterialTheme.typography.caption,
                value = descText, onValueChange = { viewModel.updateDesc(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(2.dp))
            FieldText(stringResource(R.string.TupCreateFormTags))
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .testTag("TagsTextField"),
                textStyle = MaterialTheme.typography.caption,
                value = viewModel.tags.joinToString(), onValueChange = { viewModel.updateTags(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
        }

    }
}

@Composable
private fun FieldText(text: String = "") {
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
        TupCreationScreen()
    }
}