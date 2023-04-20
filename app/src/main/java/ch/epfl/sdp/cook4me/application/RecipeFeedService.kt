package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.RecipeNote
import ch.epfl.sdp.cook4me.persistence.repository.RecipeNoteRepository
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository

/**
 * Service that handles the recipe feed
 */
class RecipeFeedService(
    private val recipeRepository: RecipeRepository = RecipeRepository(),
    private val recipeNoteRepository: RecipeNoteRepository = RecipeNoteRepository()
) {

    /**
     * Retrieves all the recipes and assigns them their notes (0 if they have none)
     * @return a list of recipes with their id with their notes
     */
    suspend fun getRecipesWithNotes(): List<RecipeNote> {
        val recipes = recipeRepository.getAll()
        val notes = recipeNoteRepository.retrieveAllRecipeNotes()
        return recipes.map { RecipeNote(it.key, notes[it.key] ?: 0,it.value) }
    }

    /**
     * Updates the note of a recipe
     * @param recipeId the id of the recipe
     * @param note the note to add to the recipe
     * @return the new note of the recipe
     */
    suspend fun updateRecipeNotes(recipeId: String, note: Int): Int {
        val currentNote = recipeNoteRepository.getRecipeNote(recipeId)
        if (currentNote === null) {
            recipeNoteRepository.addRecipeNote(recipeId, note)
        } else {
            recipeNoteRepository.updateRecipeNote(recipeId, note + currentNote)
        }
        return note + (currentNote ?: 0)
    }
}
