package ch.epfl.sdp.cook4me.ui.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.ui.common.form.CustomTextField
import ch.epfl.sdp.cook4me.ui.common.form.CustomTitleText

// TODO add images
@Composable
fun RecipeScreen(recipe: Recipe) {
    val recipeBasicInfo = "Servings: ${recipe.servings} | Cooking time: ${recipe.cookingTime} | Difficulty: ${recipe.difficulty}"
    val recipeIngredients = "Ingredients: \n ${recipe.ingredients.map{s -> "\t - $s"}.joinToString("\n")}"
    val recipeSteps = "Steps: \n ${recipe.recipeSteps.map{s -> "\t - $s"}.joinToString("\n")}"
    val fillMaxWidth = Modifier.fillMaxWidth()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomTitleText(text = recipe.name)
        }
        CustomTextField(contentDescription = "recipeInfo", onValueChange = {}, readOnly = true, value = recipeBasicInfo, modifier = fillMaxWidth)
        CustomTextField(contentDescription = "recipeIngredients", onValueChange = {}, readOnly = true, value = recipeIngredients, modifier = fillMaxWidth)
        CustomTextField(contentDescription = "recipeSteps", onValueChange = {}, readOnly = true, value = recipeSteps, modifier = fillMaxWidth)
    }
}