package ch.epfl.sdp.cook4me.application

interface RecipeService {
    suspend fun submitForm(
        recipeName: String,
        ingredients: List<String>,
        servings: Int,
        preparationSteps: List<String>,
        difficulty: String,
        preparationTime: String,
        photos: List<String>,
    )
}
