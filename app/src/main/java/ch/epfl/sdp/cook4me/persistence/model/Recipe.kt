package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
// need mutable data class to use toObjects and toObject function of firestore
data class Recipe(
    var name: String = "",
    val ingredients: List<String> = listOf(),
    val recipeSteps: List<String> = listOf(),
    val photos: List<String> = listOf(),
    val difficulty: String = "",
    val servings: Int = 0,
    val cookingTime: String = "",
)
