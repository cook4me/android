package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
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
    suspend fun addRecipeNote(id: String, note: Int, userId: String? = null) {
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
        val userVoteDoc = store.collection(USER_VOTE_PATH).whereEqualTo("id", id)
            .whereEqualTo("userId", userId).get().await().firstOrNull()

        // if already voted, update the vote
        if (userVoteDoc != null) {
            val oldVote = userVoteDoc.getLong("note")?.toInt() ?: 0
            if (oldVote + userVote < -1 || oldVote + userVote > 1) {
                // user has already voted on other device and try to vote again, do nothing
                return
            }
            userVoteDoc.reference.update("note", oldVote + userVote).await()
        } else {
            store.collection(USER_VOTE_PATH).add(mapOf("id" to id, "userId" to userId, "note" to userVote)).await()
        }

        store.collection(RECIPE_NOTE_PATH).whereEqualTo("id", id).get().await()
            .first()?.reference?.update("note", note)?.await()
    }

    /**
     * This method is used to retrieve all the notes of all the recipes.
     * @param useOnlyCache if true, only the cache will be used (useful to make app faster offline)
     * @return a map with the id of the recipe as key and the note as value
     * if there was an error while reading, the id will be -1
     */
    suspend fun retrieveAllRecipeNotes(useOnlyCache: Boolean = false): Map<String, Int> {
        val source = if (useOnlyCache) Source.CACHE else Source.DEFAULT
        val result = store.collection(RECIPE_NOTE_PATH).get(source).await()
        return result.map { it.get("id").toString() }.zip(result.map { it.getLong("note")?.toInt() ?: 0 }).toMap()
    }

    /**
     * This method is used to retrieve all the votes of a user.
     * @param userId the id of the user
     * @return a map with the id of the recipe as key and the vote as value
     */
    suspend fun retrieveAllUserVotes(userId: String, useOnlyCache: Boolean = false): Map<String, Int> {
        val source = if (useOnlyCache) {
            Source.CACHE
        } else {
            Source.DEFAULT
        }
        val result = store.collection(USER_VOTE_PATH).whereEqualTo("userId", userId).get(source).await()
        return result.map { it.get("id").toString() }.zip(result.map { it.getLong("note")?.toInt() ?: 0 }).toMap()
    }

    suspend fun deleteAll() {
        store.deleteAllDocumentsFromCollection(RECIPE_NOTE_PATH)
        store.deleteAllDocumentsFromCollection(USER_VOTE_PATH)
    }
}
