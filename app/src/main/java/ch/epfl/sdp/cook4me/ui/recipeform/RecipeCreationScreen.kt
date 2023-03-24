package ch.epfl.sdp.cook4me.ui.recipeform

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.imageSelection.ImageSelector
import ch.epfl.sdp.cook4me.ui.simpleComponent.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomDropDownMenu
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomTitleText
import ch.epfl.sdp.cook4me.ui.simpleComponent.GenericSeparators
import ch.epfl.sdp.cook4me.ui.tupperwareform.ButtonRow
import ch.epfl.sdp.cook4me.ui.tupperwareform.ComposeFileProvider
import ch.epfl.sdp.cook4me.ui.tupperwareform.Cook4MeDivider

@Composable
fun RecipeCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeCreationViewModel,
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

    Column {
        RecipeForm(
            Modifier.weight(1f),
            viewModel,
            onClickTakePhoto = { onClickTakePhoto() },
            onClickAddImage = { onClickAddImage() },
        )
        ButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            onCancelPressed = { },
            onDonePressed = { viewModel.onSubmit() },
        )
    }
}

@Composable
fun RecipeForm(
    modifier: Modifier = Modifier,
    viewModel: RecipeCreationViewModel,
    onClickTakePhoto: () -> Unit,
    onClickAddImage: () -> Unit,
) {
    val cookingTime by viewModel.cookingTime
    val difficulty by viewModel.difficulty

    var recipeName by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CustomTitleText("Add Image")
        ImageSelector(
            Modifier,
            images = viewModel.images,
            onClickAddImage = onClickAddImage,
            onClickTakePhoto = onClickTakePhoto,
            onClickImage = { /*TODO*/ }
        )
        Cook4MeDivider(Modifier.fillMaxWidth())
        CustomTitleText(stringResource(R.string.RecipeCreationRecipeTitle))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = recipeName,
            contentDescription = stringResource(R.string.RecipeNameTextFieldDesc),
            onValueChange = {
                recipeName = it
                viewModel.updateRecipeName(it)
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(stringResource(R.string.RecipeNameTextFieldPlaceholder)) }
        )
        Cook4MeDivider(Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomTitleText(stringResource(R.string.RecipeCreationIngredientsTitle))
            Spacer(Modifier.size(15.dp))
            ServingsEntry(onValueChange = { viewModel.updateServings(it) })
        }
        BulletPointTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.updateIngredients(it) },
            placeholder = { Text(stringResource(R.string.ingredientsTextFieldPlaceholder)) },
            contentDescription = stringResource(R.string.ingredientsTextFieldContentDesc)
        )
        Cook4MeDivider(Modifier.fillMaxWidth())
        CustomTitleText(stringResource(R.string.RecipePreparationTitle))
        Row {
            CookingTimeEntry(
                onValueChange = { viewModel.changeCookingTime(it) },
                listCookingTime = viewModel.cookingTimeOptions,
                value = cookingTime
            )
            DifficultyDropDownMenu(
                listDifficulty = viewModel.difficultyOptions,
                onValueChange = { viewModel.changeDifficulty(it) },
                value = difficulty
            )
        }
        BulletPointTextField(
            onValueChange = { viewModel.updateSteps(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.RecipeStepsTextFieldPlaceholder)) },
            separators = GenericSeparators.EnumeratedList,
            contentDescription = stringResource(R.string.RecipeStepsTextFieldDesc)
        )
    }
}

val cornerSize = 8.dp
val textPadding = 5.dp
val textFieldHeight = 45.dp
@Composable
fun ServingsEntry(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
) {
    var nbOfServings by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier.padding(horizontal = textPadding),
            text = stringResource(R.string.RecipeCreationScreenServingsTitle)
        )
        CustomTextField(
            textStyle = MaterialTheme.typography.caption,
            shape = RoundedCornerShape(cornerSize),
            modifier = Modifier
                .width(50.dp)
                .height(textFieldHeight),
            contentDescription = "",
            value = nbOfServings,
            onValueChange = {
                nbOfServings = it.takeWhile { c -> c.isDigit() }.take(2)
                onValueChange(nbOfServings)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun CookingTimeEntry(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    listCookingTime: List<String> = listOf(),
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(horizontal = textPadding), text = stringResource(R.string.RecipeCreationCookingTimeEntryTitle))
        CookingTimeDropDownMenu(
            listCookingTime = listCookingTime,
            modifier = Modifier
                .width(80.dp)
                .height(45.dp),
            value = value,
            onValueChange = onValueChange,
        )
    }
}

@Composable
private fun CookingTimeDropDownMenu(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    listCookingTime: List<String> = listOf(),
    value: String = "",
) {
    CustomDropDownMenu(
        textStyle = MaterialTheme.typography.caption,
        modifier = modifier,
        options = listCookingTime,
        onValueChange = onValueChange,
        value = value,
    )
}

@Composable
private fun DifficultyDropDownMenu(
    onValueChange: (String) -> Unit = {},
    listDifficulty: List<String> = listOf(),
    value: String = "",
) {

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(horizontal = textPadding), text = stringResource(R.string.RecipeCreationDifficultyTitle))
        CustomDropDownMenu(
            textStyle = MaterialTheme.typography.caption,
            modifier = Modifier.height(textFieldHeight),
            options = listDifficulty,
            onValueChange = onValueChange,
            value = value,
        )
    }
}
