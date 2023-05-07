package ch.epfl.sdp.cook4me.persistence.model

import androidx.room.Entity
import androidx.room.TypeConverters
import ch.epfl.sdp.cook4me.persistence.repository.RecipeConverter

@Entity(tableName = "recipeNote", primaryKeys = ["recipeId"])
@TypeConverters(RecipeConverter::class)
data class RecipeNote(
    val recipeId: String = "",
    val note: Int = 0,
    val recipe: Recipe = Recipe(),
)
