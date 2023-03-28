package ch.epfl.sdp.cook4me.ui.recipeform

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.RecipeService
import kotlinx.coroutines.launch

class MockRecipeService : RecipeService {
    override suspend fun submitForm(
        recipeName: String,
        ingredients: List<String>,
        servings: Int,
        preparationSteps: List<String>,
        difficulty: String,
        preparationTime: String,
        photos: List<String>,
    ) {
        Log.d("Debug", recipeName)
    }
}

class RecipeCreationViewModel(
    private val recipeService: RecipeService = MockRecipeService(),
) : ViewModel() {
    // Elements where UI state is handled by UI internally
    private data class RecipeForm(
        val name: String = "",
        val ingredients: String = "",
        val servings: Int = 0,
        val preparationSteps: String = "",
    )
    private var _recipeForm = RecipeForm()

    val cookingTimeOptions = listOf(
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

    val difficultyOptions = listOf("Easy", "Medium", "Hard")

    // Elements for which UI state is handled from viewModel
    private val _images = mutableStateListOf<Uri>()
    private var _formError = mutableStateOf(false)
    private var _cookingTime = mutableStateOf(cookingTimeOptions[3])
    private var _difficulty = mutableStateOf(difficultyOptions[0])

    val images: List<Uri> = _images
    val cookingTime: MutableState<String> = _cookingTime
    val difficulty: MutableState<String> = _difficulty

    private val minServings = 1
    private val maxServings = 99

    fun updateIngredients(ingredients: String) {
        _recipeForm = _recipeForm.copy(ingredients = ingredients)
    }

    fun updateRecipeName(name: String) {
        _recipeForm = _recipeForm.copy(name = name)
    }

    fun updateServings(servings: String) {
        try {
            _recipeForm = _recipeForm.copy(servings = servings.toInt())
        } catch (n: java.lang.NumberFormatException) {
            _formError.value = true
        }
    }

    fun updateSteps(steps: String) {
        _recipeForm = _recipeForm.copy(preparationSteps = steps)
    }
    fun changeDifficulty(difficulty: String) {
        _difficulty.value = difficulty
    }

    fun changeCookingTime(cookingTime: String) {
        _cookingTime.value = cookingTime
    }

    fun addImage(image: Uri) {
        _images.add(image)
    }

    private fun recipeIsValid(): Boolean {
        var isValid = true
        if (_recipeForm.name.isBlank()) isValid = false
        if (_difficulty.value.isBlank()) isValid = false
        if (_recipeForm.servings < minServings || _recipeForm.servings > maxServings) isValid = false
        if (_recipeForm.ingredients.isBlank()) isValid = false
        if (_recipeForm.preparationSteps.isBlank()) isValid = false
        if (_cookingTime.value.isBlank()) isValid = false
        return isValid
    }

    fun onSubmit() {
        Log.d(
            "Debug",
            "${_recipeForm.name}\n${_recipeForm.ingredients}\n${_recipeForm.preparationSteps}" +
                "\n${_difficulty.value}\n${_recipeForm.servings}\n${_cookingTime.value}"
        )
        if (recipeIsValid()) {
            _formError.value = false
            viewModelScope.launch {
                recipeService.submitForm(
                    _recipeForm.name,
                    _recipeForm.ingredients.lines().filter { it.isNotBlank() },
                    _recipeForm.servings,
                    _recipeForm.preparationSteps.lines().filter { it.isNotBlank() },
                    _difficulty.value,
                    _cookingTime.value,
                    _images.map { it.toString() }
                )
            }
        } else {
            _formError.value = true
        }
    }
}
