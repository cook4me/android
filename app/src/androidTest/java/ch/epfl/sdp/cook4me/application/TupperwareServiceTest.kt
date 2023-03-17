package ch.epfl.sdp.cook4me.application

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
class TupperwareServiceTest {
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
    fun submittingTupShouldAddTupInRepository() = runTest {
        val tupperwareService = TupperwareServiceWithRepository(tupperwareRepository)

        val tupperware = Tupperware(
            name = "title2", desc = "desc2", tags = listOf("Langosh", "Hungarian"), photos = listOf("Uri")
        )
        tupperwareService.submitForm(
            tupperware.name,
            tupperware.desc,
            tupperware.tags,
            tupperware.photos
        )
        val allTupperwares = tupperwareRepository.getAll()
        MatcherAssert.assertThat(allTupperwares.values, Matchers.containsInAnyOrder(tupperware))
    }
}
