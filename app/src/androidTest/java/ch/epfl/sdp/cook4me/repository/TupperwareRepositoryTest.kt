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
class TupperwareRepositoryTest {
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
            title = "title", description = "desc", tags = listOf("Pizza", "Italian"), images = listOf("Uri")
        )
        val newEntry2 = Tupperware(
            title = "title2", description = "desc2", tags = listOf("Langosh", "Hungarian"), images = listOf("Uri")
        )
        tupperwareRepository.add(newEntry1)
        tupperwareRepository.add(newEntry2)
        val allTupperware = tupperwareRepository.getAll()
        MatcherAssert.assertThat(allTupperware.values, Matchers.containsInAnyOrder(newEntry1, newEntry2))
    }

    @Test
    fun updateExistingTupperwareKeepOnlyRecentTupperware() = runTest {
        val entryToBeUpdated = Tupperware(
            title = "title", description = "desc", tags = listOf("Pizza", "Italian"), images = listOf("Uri")
        )
        tupperwareRepository.add(entryToBeUpdated)
        val allTupperwareBeforeUpdate = tupperwareRepository.getAll()
        val updatedEntry = entryToBeUpdated.copy(title = "updated")
        tupperwareRepository.update(allTupperwareBeforeUpdate.keys.first(), updatedEntry)
        val allTupperwareAfterUpdate = tupperwareRepository.getAll()
        MatcherAssert.assertThat(allTupperwareAfterUpdate.values, Matchers.contains(updatedEntry))
        MatcherAssert.assertThat(allTupperwareAfterUpdate.values, Matchers.not(Matchers.contains(entryToBeUpdated)))
    }

    @Test
    fun getTupperwareById() = runTest {
        tupperwareRepository.add(
            Tupperware(
                title = "title0", description = "desc0", tags = listOf("Pizza", "Hungarian"), images = listOf("Uri0")
            )
        )
        tupperwareRepository.add(
            Tupperware(
                title = "title1", description = "desc1", tags = listOf("Langosh", "Italian"), images = listOf("Uri1")
            )
        )
        tupperwareRepository.add(
            Tupperware(
                title = "title2", description = "desc2", tags = listOf("Langosh", "Hungarian"), images = listOf("Uri2")
            )
        )
        val allTupperware = tupperwareRepository.getAll()
        val actual = tupperwareRepository.getById(allTupperware.keys.first())
        MatcherAssert.assertThat(actual, Matchers.`is`(allTupperware.values.first()))
    }
}
