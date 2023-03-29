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
import androidx.compose.runtime.mutableStateListOf
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
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.ui.common.form.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.common.form.CustomDropDownMenu
import ch.epfl.sdp.cook4me.ui.common.form.CustomTextField
import ch.epfl.sdp.cook4me.ui.common.form.CustomTitleText
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.GenericSeparators
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.imageSelection.ImageSelector
import ch.epfl.sdp.cook4me.ui.tupperwareform.ComposeFileProvider
import ch.epfl.sdp.cook4me.ui.tupperwareform.CustomDivider

private val cornerSize = 8.dp
private val textPadding = 5.dp
private val textFieldHeight = 45.dp

private val cookingTimeOptions = listOf(
    "5min",
    "10min",
    "15min",
    "30min",
    "45min",
    "1h",
    "1h15",
    "1h30",
    "2h00",
    "2h30",
    "3h00",
    "3h30",
    "4h00",
    "4h30",
)

private val difficultyOptions = listOf("Easy", "Medium", "Hard")

@Composable
fun RecipeCreationScreen(
    submitForm: (Recipe) -> Unit = {}
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
            onClickTakePhoto = { onClickTakePhoto() },
            onClickAddImage = { onClickAddImage() },
            submitForm = submitForm,
            images = images,
        )
    }
}

@Composable
private fun RecipeForm(
    modifier: Modifier = Modifier,
    onClickTakePhoto: () -> Unit,
    onClickAddImage: () -> Unit,
    submitForm: (Recipe) -> Unit = {},
    images: List<Uri> = listOf(),
) {
    val context = LocalContext.current
    val recipeNameState = remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val ingredientsState = remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val preparationStepsState = remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val servingsState = remember { RequiredTextFieldState(context.getString(R.string.TupCreateBlank)) }
    val cookingTimeState = remember {
        RequiredTextFieldState(context.getString(R.string.TupCreateBlank), cookingTimeOptions.first())
    }
    val difficultyState = remember {
        RequiredTextFieldState(context.getString(R.string.TupCreateBlank), difficultyOptions.first())
    }

    val formIsValid = recipeNameState.isValid && ingredientsState.isValid &&
        preparationStepsState.isValid && servingsState.isValid &&
        cookingTimeState.isValid && difficultyState.isValid

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CustomTitleText(stringResource(R.string.RecipeCreationScreenAddImageTitle))
        ImageSelector(
            Modifier,
            images = images,
            onClickAddImage = onClickAddImage,
            onClickTakePhoto = onClickTakePhoto,
            onClickImage = { /*TODO*/ }
        )
        CustomDivider(Modifier.fillMaxWidth())
        CustomTitleText(stringResource(R.string.RecipeCreationRecipeTitle))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = recipeNameState.text,
            contentDescription = stringResource(R.string.RecipeNameTextFieldDesc),
            onValueChange = {
                recipeNameState.text = it
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(stringResource(R.string.RecipeNameTextFieldPlaceholder)) }
        )
        CustomDivider(Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomTitleText(stringResource(R.string.RecipeCreationIngredientsTitle))
            Spacer(Modifier.size(15.dp))
            ServingsEntry(value = servingsState.text, onValueChange = { servingsState.text = it })
        }
        BulletPointTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { ingredientsState.text = it },
            placeholder = { Text(stringResource(R.string.ingredientsTextFieldPlaceholder)) },
            contentDescription = stringResource(R.string.ingredientsTextFieldContentDesc)
        )
        CustomDivider(Modifier.fillMaxWidth())
        CustomTitleText(stringResource(R.string.RecipePreparationTitle))
        Row {
            CookingTimeEntry(
                onValueChange = { cookingTimeState.text = it },
                listCookingTime = cookingTimeOptions,
                value = cookingTimeState.text,
                contentDescription = stringResource(R.string.RecipeCreationCookingTimeDropDownMenuDesc)
            )
            DifficultyDropDownMenu(
                listDifficulty = difficultyOptions,
                onValueChange = { difficultyState.text = it },
                value = difficultyState.text,
                contentDescription = stringResource(R.string.RecipeCreationDifficultyDropDownMenuDesc)
            )
        }
        BulletPointTextField(
            onValueChange = { preparationStepsState.text = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.RecipeStepsTextFieldPlaceholder)) },
            separators = GenericSeparators.EnumeratedList,
            contentDescription = stringResource(R.string.RecipeStepsTextFieldDesc)
        )
    }
    FormButtons(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onCancelText = R.string.ButtonRowCancel,
        onSaveText = R.string.ButtonRowDone,
        onCancelClick = { },
        onSaveClick = {
            recipeNameState.enableShowErrors()
            ingredientsState.enableShowErrors()
            preparationStepsState.enableShowErrors()
            difficultyState.enableShowErrors()
            cookingTimeState.enableShowErrors()
            servingsState.enableShowErrors()
            if (formIsValid) {
                submitForm(
                    Recipe(
                        name = recipeNameState.text,
                        ingredients = ingredientsState.text.lines().filter { it.isNotBlank() },
                        recipeSteps = preparationStepsState.text.lines().filter { it.isNotBlank() },
                        servings = servingsState.text.toInt(),
                        difficulty = difficultyState.text,
                        cookingTime = cookingTimeState.text,
                        photos = listOf()
                    )
                )
            } else {
                // TODO: show snackbar
            }
        },
    )
}

@Composable
fun ServingsEntry(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
) {
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
            contentDescription = stringResource(R.string.RecipeCreationServingsTextFieldDesc),
            value = value,
            onValueChange = {
                onValueChange(
                    it.takeWhile { c -> c.isDigit() }.take(2)
                )
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
    contentDescription: String
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = textPadding),
            text = stringResource(R.string.RecipeCreationCookingTimeEntryTitle)
        )
        CookingTimeDropDownMenu(
            listCookingTime = listCookingTime,
            modifier = Modifier
                .width(80.dp)
                .height(45.dp),
            value = value,
            onValueChange = onValueChange,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun CookingTimeDropDownMenu(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    listCookingTime: List<String> = listOf(),
    value: String = "",
    contentDescription: String
) {
    CustomDropDownMenu(
        textStyle = MaterialTheme.typography.caption,
        modifier = modifier,
        options = listCookingTime,
        onValueChange = onValueChange,
        value = value,
        contentDescription = contentDescription,
    )
}

@Composable
private fun DifficultyDropDownMenu(
    onValueChange: (String) -> Unit = {},
    listDifficulty: List<String> = listOf(),
    value: String = "",
    contentDescription: String
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = textPadding),
            text = stringResource(R.string.RecipeCreationDifficultyTitle)
        )
        CustomDropDownMenu(
            textStyle = MaterialTheme.typography.caption,
            modifier = Modifier.height(textFieldHeight),
            options = listDifficulty,
            onValueChange = onValueChange,
            value = value,
            contentDescription = contentDescription
        )
    }
}
