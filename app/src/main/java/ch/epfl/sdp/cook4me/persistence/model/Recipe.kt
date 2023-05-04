package ch.epfl.sdp.cook4me.persistence.model

import com.google.firebase.Timestamp

@Suppress("DataClassShouldBeImmutable")
// need mutable data class to use toObjects and toObject function of firestore
data class Recipe(
    val user: String = "",
    val name: String = "",
    val id: Int = 0,
    val ingredients: List<String> = listOf(),
    val recipeSteps: List<String> = listOf(),
    val photos: List<String> = listOf(),
    val difficulty: String = "",
    val servings: Int = 0,
    val cookingTime: String = "",
    val creationTime: Timestamp = Timestamp.now(),
) {
    /**
     * If all fields same except creationTime, then they are same
     * since creationTime is modified when recipe is added to firestore
     * @param other: Any?
     * @return Boolean if two recipes are same
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Recipe) return false
        return user == other.user &&
            name == other.name &&
            id == other.id &&
            ingredients == other.ingredients &&
            recipeSteps == other.recipeSteps &&
            photos == other.photos &&
            difficulty == other.difficulty &&
            servings == other.servings &&
            cookingTime == other.cookingTime
    }
}
