package ch.epfl.sdp.cook4me.persistence.model

import androidx.room.Entity

@Entity(tableName = "recipeNote", primaryKeys = ["recipeId"])
data class RecipeNote(
    val recipeId: String = "",
    val note: Int = 0,
    val recipe: Recipe = Recipe(),
)
