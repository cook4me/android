package ch.epfl.sdp.cook4me.persistence.repository

import androidx.room.TypeConverter
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.gson.Gson

class RecipeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromRecipe(recipe: Recipe): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun toRecipe(recipe: String): Recipe {
        return gson.fromJson(recipe, Recipe::class.java)
    }
}