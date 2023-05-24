package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.RecipeNote
import ch.epfl.sdp.cook4me.persistence.repository.RecipeNoteRepository
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import java.util.logging.Logger

/**
 * Service that handles the recipe feed (get and update notes)
 * @param recipeRepository the repository of recipes
 * @param recipeNoteRepository the repository of recipe notes
 * @param accountService the service that handles the account (to get the current user)
 */
class RecipeFeedService(
    private val recipeRepository: RecipeRepository = RecipeRepository(),
    private val recipeNoteRepository: RecipeNoteRepository = RecipeNoteRepository(),
    private val accountService: AccountService = AccountService(),
    private val isOnline: Boolean = true
) {
    /**
     * Retrieves all the recipes and assigns them their notes (0 if they have none)
     * @return a list of recipes with their id with their notes
     */
    suspend fun getRecipesWithNotes(): List<RecipeNote> {
        val recipes = recipeRepository.getAll(useOnlyCache = !isOnline)
        val notes = recipeNoteRepository.retrieveAllRecipeNotes(useOnlyCache = !isOnline)
        return recipes.map { RecipeNote(it.key, notes[it.key] ?: 0, it.value) }
    }

    /**
     * Updates the note of a recipe
     * @param recipeId the id of the recipe
     * @param note the note to add to the recipe
     * @return the new note of the recipe
     */
    suspend fun updateRecipeNotes(recipeId: String, note: Int): Int {
        val userId = accountService.getCurrentUserWithEmail()
        val currentNote = recipeNoteRepository.getRecipeNote(recipeId)

        if (userId === null) {
            Logger.getGlobal().warning("User not logged in")
            return currentNote ?: 0
        }

        if (currentNote === null) {
            recipeNoteRepository.addRecipeNote(recipeId, note, userId)
        } else {
            recipeNoteRepository.updateRecipeNote(recipeId, note + currentNote, userId, note)
        }

        return note + (currentNote ?: 0)
    }

    /**
     * Retrieves the personal votes of the user
     * @return a map of recipe id to the vote of the user
     */
    suspend fun getRecipePersonalVotes(): Map<String, Int> {
        val userId = accountService.getCurrentUserWithEmail()
        if (userId === null) {
            return mapOf()
        }
        return recipeNoteRepository.retrieveAllUserVotes(userId, useOnlyCache = !isOnline)
    }

    suspend fun getRecipeImage(id: String) = recipeRepository.getRecipeImage(id)
}
