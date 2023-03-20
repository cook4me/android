package ch.epfl.sdp.cook4me.ui.recipeform

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.simpleComponent.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomDropDownMenu
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.CustomTitleText
import ch.epfl.sdp.cook4me.ui.simpleComponent.GenericSeparators
import ch.epfl.sdp.cook4me.ui.tupperwareform.ButtonRow
import ch.epfl.sdp.cook4me.ui.tupperwareform.Cook4MeDivider
import ch.epfl.sdp.cook4me.ui.tupperwareform.ImageSelector

@Composable
fun RecipeCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeCreationViewModel,
) {
    Column {
        RecipeForm(
            Modifier.weight(1f),
            viewModel,
        )
        ButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            onCancelPressed = {  },
            onDonePressed = { viewModel.onSubmit() },
        )
    }
}

@Composable
fun RecipeForm(
    modifier: Modifier = Modifier,
    viewModel: RecipeCreationViewModel,
) {
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
            onClickAddImage = { /*TODO*/ },
            onClickTakePhoto = { /*TODO*/ },
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
            placeholder = {Text(stringResource(R.string.RecipeNameTextFieldPlaceholder))}
        )
        Cook4MeDivider(Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            CustomTitleText(stringResource(R.string.RecipeCreationIngredientsTitle))
            Spacer(Modifier.size(15.dp))
            ServingsEntry(onValueChange = { viewModel.updateServings(it) })
        }
        BulletPointTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.updateIngredients(it) },
            placeholder = {Text(stringResource(R.string.ingredientsTextFieldPlaceholder))},
            contentDescription = stringResource(R.string.ingredientsTextFieldContentDesc)
        )
        Cook4MeDivider(Modifier.fillMaxWidth())
        CustomTitleText(stringResource(R.string.RecipePreparationTitle))
        Row {
            CookingTimeEntry(onValueChange = { viewModel.updatePrepTime(it) })
            DifficultyDropDownMenu(onValueChange = { viewModel.updateDifficulty(it) })
        }
        BulletPointTextField(
            onValueChange = { viewModel.updateSteps(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.RecipeStepsTextFieldPlaceholder))},
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
    onValueChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(horizontal = textPadding), text = "Cooking Time")
        CookingTimeDropDownMenu(
            modifier = Modifier.width(80.dp).height(45.dp),
            onValueChange = onValueChange,
        )
    }
}

@Composable
private fun CookingTimeDropDownMenu(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    listCookingTime: List<String> = listOf()
) {
    val listCookingTime = listOf(
        "5min",
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

    CustomDropDownMenu(
        textStyle = MaterialTheme.typography.caption,
        modifier = modifier,
        options = listCookingTime,
        onValueChange = onValueChange,
        defaultValue = listCookingTime[2]
    )
}

@Composable
private fun DifficultyDropDownMenu(
    onValueChange: (String) -> Unit = {}
) {
    val difficulty = listOf("Easy", "Medium", "Hard")
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(horizontal = textPadding), text = stringResource(R.string.RecipeCreationDifficultyTitle))
        CustomDropDownMenu(
            textStyle = MaterialTheme.typography.caption,
            modifier = Modifier.height(textFieldHeight),
            options = difficulty,
            onValueChange = onValueChange,
            defaultValue = difficulty[0]
        )
    }
}