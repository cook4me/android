package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val COLLECTION_PATH = "tupperwares"

@ExperimentalCoroutinesApi
class TupRepositoryTest {
    private lateinit var tupperwareRepository: TupperwareRepository
    private lateinit var store: FirebaseFirestore

    @Before
    fun setUp() {
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        tupperwareRepository = TupperwareRepository(store)
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshot in querySnapshot.documents) {
                store.collection(COLLECTION_PATH).document(documentSnapshot.id).delete().await()
            }
        }
    }

    @Test
    fun storeNewTupperwares() = runTest {
        val newEntry1 = Tupperware(
            name = "title", desc = "desc", tags = listOf("Pizza", "Italian"), photos = listOf("Uri")
        )
        val newEntry2 = Tupperware(
            name = "title2", desc = "desc2", tags = listOf("Langosh", "Hungarian"), photos = listOf("Uri")
        )
        tupperwareRepository.add(newEntry1)
        tupperwareRepository.add(newEntry2)
        val allRecipes = tupperwareRepository.getAll()
        MatcherAssert.assertThat(allRecipes.values, Matchers.containsInAnyOrder(newEntry1, newEntry2))
    }

    @Test
    fun updateExistingTupperwareKeepOnlyRecentTupperware() = runTest {
        val entryToBeUpdated = Tupperware(
            name = "title", desc = "desc", tags = listOf("Pizza", "Italian"), photos = listOf("Uri")
        )
        tupperwareRepository.add(entryToBeUpdated)
        val allRecipesBeforeUpdate = tupperwareRepository.getAll()
        val updatedEntry = entryToBeUpdated.copy(name = "updated")
        tupperwareRepository.update(allRecipesBeforeUpdate.keys.first(), updatedEntry)
        val allRecipesAfterUpdate = tupperwareRepository.getAll()
        MatcherAssert.assertThat(allRecipesAfterUpdate.values, Matchers.contains(updatedEntry))
        MatcherAssert.assertThat(allRecipesAfterUpdate.values, Matchers.not(Matchers.contains(entryToBeUpdated)))
    }

    @Test
    fun getRecipeById() = runTest {
        tupperwareRepository.add(
            Tupperware(
                name = "title0", desc = "desc0", tags = listOf("Pizza", "Hungarian"), photos = listOf("Uri0")
            )
        )
        tupperwareRepository.add(
            Tupperware(
                name = "title1", desc = "desc1", tags = listOf("Langosh", "Italian"), photos = listOf("Uri1")
            )
        )
        tupperwareRepository.add(
            Tupperware(
                name = "title2", desc = "desc2", tags = listOf("Langosh", "Hungarian"), photos = listOf("Uri2")
            )
        )
        val allRecipes = tupperwareRepository.getAll()
        val actual = tupperwareRepository.getById(allRecipes.keys.first())
        MatcherAssert.assertThat(actual, Matchers.`is`(allRecipes.values.first()))
    }
}
