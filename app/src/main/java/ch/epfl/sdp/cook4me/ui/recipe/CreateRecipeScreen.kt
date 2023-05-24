package ch.epfl.sdp.cook4me.ui.recipe

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import ch.epfl.sdp.cook4me.ui.common.ImageSelector
import ch.epfl.sdp.cook4me.ui.common.form.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.common.form.CustomTextField
import ch.epfl.sdp.cook4me.ui.common.form.CustomTitleText
import ch.epfl.sdp.cook4me.ui.common.form.DropDownMenuWithTitle
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.form.FormState
import ch.epfl.sdp.cook4me.ui.common.form.FormTitle
import ch.epfl.sdp.cook4me.ui.common.form.GenericSeparators
import ch.epfl.sdp.cook4me.ui.common.form.ImageFieldState
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.tupperware.form.ComposeFileProvider
import ch.epfl.sdp.cook4me.ui.tupperware.form.CustomDivider
import kotlinx.coroutines.launch

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
fun CreateRecipeScreen(
    repository: RecipeRepository = RecipeRepository(),
    onCancelClick: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = {}
) {
    var imageState by remember { mutableStateOf(ImageFieldState("Recipe needs an image")) }
    val scope = rememberCoroutineScope()

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
            onImageClick = { imageState.image = null },
            onCancelClick = onCancelClick,
            imageState = imageState,
            submitForm = { recipe ->
                scope.launch {
                    repository.add(recipe, imageState.image)
                    onSuccessfulSubmit()
                }
            }
        )
    }
}

@Composable
private fun RecipeForm(
    modifier: Modifier = Modifier,
    onClickTakePhoto: () -> Unit,
    onClickAddImage: () -> Unit,
    onCancelClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    submitForm: (Recipe) -> Unit = {},
    imageState: ImageFieldState,
) {
    var inProgress by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val recipeNameState = remember { RequiredTextFieldState("Recipe needs a name") }
    val ingredientsState = remember { RequiredTextFieldState("Recipe needs ingredients") }
    val preparationStepsState = remember { RequiredTextFieldState("Recipe needs instructions") }
    val servingsState = remember { RequiredTextFieldState("Recipe needs a number of servings") }
    val cookingTimeState = remember {
        RequiredTextFieldState("Recipe needs a cooking time", cookingTimeOptions.first())
    }
    val difficultyState = remember {
        RequiredTextFieldState("Recipe needs a difficulty", difficultyOptions.first())
    }
    val form = FormState(
        recipeNameState,
        ingredientsState,
        preparationStepsState,
        servingsState,
        cookingTimeState,
        difficultyState,
        imageState,
    )
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    @Composable
    fun BottomBar() {
        FormButtons(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            isLoading = inProgress,
            onCancelText = R.string.ButtonRowCancel,
            onSaveText = R.string.ButtonRowDone,
            onCancelClick = onCancelClick,
            onSaveClick = {
                form.enableShowErrors()
                if (form.isValid) {
                    inProgress = true
                    submitForm(
                        Recipe(
                            name = recipeNameState.text,
                            ingredients = ingredientsState.text.lines().filter { it.isNotBlank() },
                            recipeSteps = preparationStepsState.text.lines().filter { it.isNotBlank() },
                            servings = servingsState.text.toInt(),
                            difficulty = difficultyState.text,
                            cookingTime = cookingTimeState.text,
                        )
                    )
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(form.getFirstError())
                    }
                }
            },
        )
    }

    @Composable
    fun RecipeFields(paddingValues: PaddingValues) {
        Column(
            modifier = modifier
                .testTag(stringResource(R.string.create_recipe_screen_tag))
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FormTitle(modifier = Modifier.padding(10.dp),title = "Create a Recipe")
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                ImageSelector(
                    Modifier,
                    image = imageState.image,
                    onClickAddImage = onClickAddImage,
                    onClickTakePhoto = onClickTakePhoto,
                    onClickImage = onImageClick,
                    imageSize = 250.dp,
                    isError = imageState.showErrors()
                )
            }
            //CustomDivider()
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
                placeholder = { Text(stringResource(R.string.RecipeNameTextFieldPlaceholder)) },
                isError = recipeNameState.showErrors()
            )
            //CustomDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CustomTitleText(stringResource(R.string.RecipeCreationIngredientsTitle))
                Spacer(Modifier.size(15.dp))
                ServingsEntry(
                    value = servingsState.text,
                    onValueChange = { servingsState.text = it },
                    isError = servingsState.showErrors()
                )
            }
            BulletPointTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { ingredientsState.text = it },
                placeholder = { Text(stringResource(R.string.ingredientsTextFieldPlaceholder)) },
                contentDescription = stringResource(R.string.ingredientsTextFieldContentDesc),
                isError = ingredientsState.showErrors()
            )
            //CustomDivider()
            CustomTitleText(stringResource(R.string.RecipePreparationTitle))
            Row {
                CookingTimeEntry(
                    onValueChange = { cookingTimeState.text = it },
                    value = cookingTimeState.text,
                )
                DifficultyEntry(
                    onValueChange = { difficultyState.text = it },
                    value = difficultyState.text,
                )
            }
            BulletPointTextField(
                onValueChange = { preparationStepsState.text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(R.string.RecipeStepsTextFieldPlaceholder)) },
                separators = GenericSeparators.EnumeratedList,
                contentDescription = stringResource(R.string.RecipeStepsTextFieldDesc),
                isError = preparationStepsState.showErrors()
            )
        }
    }

    Scaffold(
        modifier = Modifier.padding(10.dp),
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar() }
    ) {
        RecipeFields(it)
    }

}

@Composable
fun ServingsEntry(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    isError: Boolean,
) {
    Row(
        modifier = modifier,
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isError
        )
    }
}

@Composable
private fun CookingTimeEntry(
    value: String,
    onValueChange: (String) -> Unit
) {
    DropDownMenuWithTitle(
        contentDescription = stringResource(R.string.RecipeCreationCookingTimeDropDownMenuDesc),
        height = 45.dp,
        width = 80.dp,
        options = cookingTimeOptions,
        titleText = stringResource(R.string.RecipeCreationCookingTimeEntryTitle),
        value = value,
        onValueChange = onValueChange
    )
}

@Composable
private fun DifficultyEntry(
    value: String,
    onValueChange: (String) -> Unit
) {
    DropDownMenuWithTitle(
        contentDescription = stringResource(R.string.RecipeCreationDifficultyDropDownMenuDesc),
        height = textFieldHeight,
        options = difficultyOptions,
        titleText = stringResource(R.string.RecipeCreationDifficultyTitle),
        value = value,
        onValueChange = onValueChange
    )
}
