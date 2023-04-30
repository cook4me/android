package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

const val RECIPE_NOTE_PATH = "recipeNotes"
const val USER_VOTE_PATH = "userVotes"

/**
 * This class is used to store and retrieve notes for recipes.
 * @param store the FireStore database
 */
class RecipeNoteRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    /**
     * This method is used to get the note of a recipe.
     * @param id the id of the recipe
     * @return the note of the recipe, null if the recipe has no note
     */
    suspend fun getRecipeNote(id: String): Int? = try {
        store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.getLong("note")?.toInt()
    } catch (e: NoSuchElementException) {
        println("No note for recipe $id (err: ${e.message})")
        null
    }

    /**
     * This method is used to add a note to a recipe.
     * @param id the id of the recipe
     * @param note the note of the recipe
     * @param userId the id of the user (email) who added the note (null if empty vote)
     */
    suspend fun addRecipeNote(id: String, note: Int, userId: String?) {
        store.collection(RECIPE_NOTE_PATH).add(mapOf("id" to id, "note" to note)).await()
        if (userId != null) {
            store.collection(USER_VOTE_PATH).add(mapOf("id" to id, "userId" to userId, "note" to note)).await()
        }
    }

    /**
     * This method is used to update the note of a recipe.
     * @param id the id of the recipe
     * @param note the new note of the recipe
     * @param userId the id of the user (email) who updated the note
     * @param userVote the relative change of the vote of the user
     */
    suspend fun updateRecipeNote(id: String, note: Int, userId: String, userVote: Int) {
        store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.reference?.update("note", note)?.await()

        val userVoteDoc = store.collection(USER_VOTE_PATH).whereEqualTo("id", id)
            .whereEqualTo("userId", userId).get().await().firstOrNull()

        // if already voted, update the vote
        if (userVoteDoc != null) {
            val oldVote = userVoteDoc.getLong("note")?.toInt() ?: 0
            userVoteDoc.reference.update("note", oldVote + userVote).await()
        } else {
            store.collection(USER_VOTE_PATH).add(mapOf("id" to id, "userId" to userId, "note" to userVote)).await()
        }
    }

    /**
     * This method is used to retrieve all the notes of all the recipes.
     * @return a map with the id of the recipe as key and the note as value
     * if there was an error while reading, the id will be -1
     */
    suspend fun retrieveAllRecipeNotes(): Map<String, Int> {
        val result = store.collection(RECIPE_NOTE_PATH).get().await()
        return result.map { it.get("id").toString() }.zip(result.map { it.getLong("note")?.toInt() ?: 0 }).toMap()
    }

    /**
     * This method is used to retrieve all the votes of a user.
     * @param userId the id of the user
     * @return a map with the id of the recipe as key and the vote as value
     */
    suspend fun retrieveAllUserVotes(userId: String): Map<String, Int> {
        val result = store.collection(USER_VOTE_PATH).whereEqualTo("userId", userId).get().await()
        return result.map { it.get("id").toString() }.zip(result.map { it.getLong("note")?.toInt() ?: 0 }).toMap()
    }
}
