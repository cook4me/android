package ch.epfl.sdp.cook4me.persistence.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RecipeNoteRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val recipeNoteRepository: RecipeNoteRepository = RecipeNoteRepository(store)

    @After
    fun cleanUp() {
        runBlocking {
            recipeNoteRepository.deleteAll()
        }
    }

    @Test
    fun storeNewRecipeNotes() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        recipeNoteRepository.addRecipeNote("recipeId2", 2)
        val allRecipeNotes = recipeNoteRepository.retrieveAllRecipeNotes()
        assertThat(allRecipeNotes.values, containsInAnyOrder(1, 2))
        assertThat(allRecipeNotes.keys, containsInAnyOrder("recipeId", "recipeId2"))
    }

    @Test
    fun updateRecipeNoteReturnsTheNewNote() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        recipeNoteRepository.addRecipeNote("recipeId2", 2)
        recipeNoteRepository.updateRecipeNote("recipeId", 3, "a@gmail.com", 0)
        val allRecipeNotes = recipeNoteRepository.retrieveAllRecipeNotes()
        assertThat(allRecipeNotes.values, containsInAnyOrder(3, 2))
    }

    @Test
    fun getRecipeNoteReturnNullIfRecipeNoteDoesNotExist() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        val recipeNote = recipeNoteRepository.getRecipeNote("recipeId3")
        assertThat(recipeNote, `is`(nullValue()))
    }

    @Test
    fun getRecipeNoteReturnRecipeNoteIfRecipeNoteExists() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        val recipeNote = recipeNoteRepository.getRecipeNote("recipeId")
        assertThat(recipeNote, `is`(1))
    }

    @Test
    fun addRecipeNoteWithUserCreatesNewUserVote() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1, "user")
        val userVote = recipeNoteRepository.retrieveAllUserVotes("user").get("recipeId")
        assertThat(userVote, `is`(1))
    }

    @Test
    fun updateRecipeNoteWithNotVotedUserCreateNewUserVote() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        recipeNoteRepository.updateRecipeNote("recipeId", 2, "user", 1)
        val userVote = recipeNoteRepository.retrieveAllUserVotes("user")["recipeId"]
        assertThat(userVote, `is`(1))
    }

    @Test
    fun updateRecipeNoteWithVotedUserUpdatesUserVote() = runTest {
        // here user clicks on upvote twice => final vote is 0
        recipeNoteRepository.addRecipeNote("recipeId", 1, "user")
        recipeNoteRepository.updateRecipeNote("recipeId", 0, "user", -1)
        val userVote = recipeNoteRepository.retrieveAllUserVotes("user")["recipeId"]
        assertThat(userVote, `is`(0))
    }

    @Test
    fun tryToUpdateTwiceSameRecipeNoteDoesNothingSecondTime() = runTest {
        recipeNoteRepository.addRecipeNote("recipeId", 1)
        recipeNoteRepository.updateRecipeNote("recipeId", 2, "user", 1)
        recipeNoteRepository.updateRecipeNote("recipeId", 2, "user", 1)
        val userVote = recipeNoteRepository.retrieveAllUserVotes("user")["recipeId"]
        assertThat(userVote, `is`(1))
    }
}
