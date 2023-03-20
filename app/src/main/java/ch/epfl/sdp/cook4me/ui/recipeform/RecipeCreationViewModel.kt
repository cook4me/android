package ch.epfl.sdp.cook4me.ui.recipeform

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
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

class RecipeCreationViewModel(private val recipeService: RecipeService = MockRecipeService()): ViewModel() {
    //Elements where UI state is handled by UI internally
    private data class RecipeForm(
        val name: String = "",
        val ingredients: String = "",
        val servings: Int = 0,
        val preparationSteps: String = "",
        val difficulty: String = "",
        val prepTime: String = "",
    )
    private var _recipeForm = RecipeForm()
    //Elements for which UI state is handled externally
    private val _images = mutableStateListOf<Uri>()
    private var _formError = mutableStateOf(false)

    val images: List<Uri> = _images
    val formError: State<Boolean> = _formError

    fun updateIngredients(ingredients: String) {
        _recipeForm = _recipeForm.copy(ingredients=ingredients)
    }

    fun updateRecipeName(name: String) {
        _recipeForm = _recipeForm.copy(name=name)
    }

    fun updateServings(servings: String) {
        try {
            _recipeForm = _recipeForm.copy(servings = servings.toInt())
        } catch (n: java.lang.NumberFormatException) {
            _formError.value = true
        }
    }

    fun updateSteps(steps: String) {
        _recipeForm = _recipeForm.copy(preparationSteps=steps)
    }
    fun updateDifficulty(difficulty: String) {
        _recipeForm = _recipeForm.copy(difficulty=difficulty)
    }

    fun updatePrepTime(prepTime: String) {
        _recipeForm = _recipeForm.copy(prepTime = prepTime)
    }

    fun updateImages(image: Uri) {
        _images.add(image)
    }

    private fun recipeIsValid(): Boolean {
        if (_recipeForm.name.isBlank()) return false
        if (_recipeForm.difficulty.isBlank()) return false
        if (_recipeForm.servings < 0 || _recipeForm.servings > 99) return false
        if (_recipeForm.ingredients.isBlank()) return false
        if (_recipeForm.preparationSteps.isBlank()) return false
        if (_recipeForm.prepTime.isBlank()) return false
        return true
    }

    fun onSubmit() {
        Log.d("Debug",
            "${_recipeForm.name}\n${_recipeForm.ingredients}\n${_recipeForm.preparationSteps}" +
                    "\n${_recipeForm.difficulty}\n${_recipeForm.servings}\n${_recipeForm.prepTime}"
        )
        if (recipeIsValid()) {
            _formError.value = false
            viewModelScope.launch {
                recipeService.submitForm(
                    _recipeForm.name,
                    _recipeForm.ingredients.lines().filter { it.isNotBlank() },
                    _recipeForm.servings,
                    _recipeForm.preparationSteps.lines().filter { it.isNotBlank() },
                    _recipeForm.difficulty,
                    _recipeForm.prepTime,
                    _images.map { it.toString() }
                )
            }
        } else {
            _formError.value = true
        }
    }
}