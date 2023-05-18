package ch.epfl.sdp.cook4me.persistence.model
data class RecipeNote(
    val recipeId: String = "",
    val note: Int = 0,
    val recipe: Recipe = Recipe(),
)
