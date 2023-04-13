package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

const val RECIPE_NOTE_PATH = "recipeNotes"

/**
 * This class is used to store and retrieve notes for recipes.
 */
class RecipeNoteRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    /**
     * This method is used to get the note of a recipe.
     * @param id the id of the recipe
     * @return the note of the recipe, null if the recipe has no note
     */
    suspend fun getRecipeNote(id: Int): Int?{
        return store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.getLong("note")?.toInt()
    }

    /**
     * This method is used to add a note to a recipe.
     * @param id the id of the recipe
     */
    suspend fun addRecipeNote(id: Int, note: Int) {
        store.collection(RECIPE_NOTE_PATH).add(mapOf("id" to id, "note" to note)).await()
    }

    /**
     * This method is used to update the note of a recipe.
     * @param id the id of the recipe
     * @param note the new note of the recipe
     */
    suspend fun updateRecipeNote(id: Int, note: Int) {
        store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.reference?.update("note", note)?.await()
    }

    /**
     * This method is used to delete the note of a recipe.
     * @param id the id of the recipe
     */
    suspend fun deleteRecipeNoteById(id: Int) {
        store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.reference?.delete()?.await()
    }

    /**
     * This method is used to retrieve all the notes of all the recipes.
     * @return a map with the id of the recipe as key and the note as value
     * if there was an error while reading, the id will be -1
     */
    suspend fun retrieveAllRecipeNotes(): Map<Int, Int> {
        val result = store.collection(RECIPE_NOTE_PATH).get().await()
        return result.map { it.getLong("id")?.toInt()?: -1 }.zip(result.map { it.getLong("note")?.toInt()?: 0 }).toMap()
    }
}