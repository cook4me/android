package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeNoteRepository
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Test

class RecipeFeedServiceTest {
    private val mockRecipeRepository = mockk<RecipeRepository>(relaxed = true)
    private val mockRecipeNoteRepository = mockk<RecipeNoteRepository>(relaxed = true)

    private val recipeFeedService = RecipeFeedService(mockRecipeRepository, mockRecipeNoteRepository)

    @Test
    fun getRecipesWithNotesReturnsListOfRecipesWithNotes() = runBlocking {
        coEvery { mockRecipeRepository.getAll() } returns mapOf("id1" to Recipe(), "id2" to Recipe())
        coEvery { mockRecipeNoteRepository.retrieveAllRecipeNotes() } returns mapOf("id1" to 1, "id2" to 2)
        val result = recipeFeedService.getRecipesWithNotes()
        assertThat(result.map { it.first.first }, containsInAnyOrder("id1", "id2"))
        assertThat(result.map { it.second }, containsInAnyOrder(1, 2))
    }

    @Test
    fun getRecipeWithNoNoteReturnsDefaultNote() = runBlocking {
        coEvery { mockRecipeRepository.getAll() } returns mapOf("id1" to Recipe(), "id2" to Recipe())
        coEvery { mockRecipeNoteRepository.retrieveAllRecipeNotes() } returns mapOf("id1" to 1)
        val result = recipeFeedService.getRecipesWithNotes()
        assertThat(result.map { it.first.first }, containsInAnyOrder("id1", "id2"))
        assertThat(result.map { it.second }, containsInAnyOrder(1, 0))
    }

    @Test
    fun updateExistingRecipeNoteUpdatesNotes() = runBlocking {
        val recipeId = "id1"

        coEvery { mockRecipeNoteRepository.getRecipeNote(recipeId) } returns 1
        coEvery { mockRecipeNoteRepository.updateRecipeNote(recipeId, 2) } returns Unit

        val newNote = recipeFeedService.updateRecipeNotes(recipeId, 2)

        // assert updateRecipeNote was called with the correct parameters
        coVerify {
            mockRecipeNoteRepository.updateRecipeNote(recipeId, 2 + 1)
        }

        assertThat(newNote, `is`(3))
    }

    @Test
    fun updatingNonExistingRecipeNoteCreatesNote() = runBlocking {
        val recipeId = "id1"

        coEvery { mockRecipeNoteRepository.getRecipeNote(recipeId) } returns null
        coEvery { mockRecipeNoteRepository.addRecipeNote(recipeId, 2) } returns Unit

        val newNote = recipeFeedService.updateRecipeNotes(recipeId, 2)

        // assert addRecipeNote was called with the correct parameters
        coVerify {
            mockRecipeNoteRepository.addRecipeNote(recipeId, 2)
        }

        assertThat(newNote, `is`(2))
    }
}
