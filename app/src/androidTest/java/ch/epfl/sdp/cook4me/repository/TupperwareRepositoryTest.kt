package ch.epfl.sdp.cook4me.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

private const val COLLECTION_PATH = "tupperwares"
private const val USER_NAME = "harry.potter@epfl.ch"

@ExperimentalCoroutinesApi
class TupperwareRepositoryTest {
    private lateinit var tupperwareRepository: TupperwareRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        storage = FirebaseStorage.getInstance()
        storage.useEmulator("10.0.2.2", 9199)
        auth = FirebaseAuth.getInstance()
        tupperwareRepository = TupperwareRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, "123456").await()
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshot in querySnapshot.documents) {
                tupperwareRepository.delete(documentSnapshot.id)
            }
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeNewTupperware() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val urls = files.map { Uri.fromFile(it) }
        val id1 = tupperwareRepository.add(title = "title1", description = "desc1", images = listOf())
        tupperwareRepository.add(
            title = "title2",
            description = "desc2",
            images = listOf(urls.first())
        )
        tupperwareRepository.add(
            title = "title3",
            description = "desc3",
            images = urls.drop(1)
        )
        val allTupperware = tupperwareRepository.getAll<Tupperware>()
        assertThat(
            allTupperware.values,
            containsInAnyOrder(
                Tupperware("title1", "desc1", USER_NAME),
                Tupperware("title2", "desc2", USER_NAME),
                Tupperware("title3", "desc3", USER_NAME),
            )
        )
        val tupIdsSortedbyTitle = allTupperware.toList().sortedBy { it.second.title }.map { it.first }
        val userTupperwareFolder = storage.reference.child("images/$USER_NAME/tupperwares")
        val tupperwareFolders = userTupperwareFolder.listAll().await()
        assertThat(
            tupperwareFolders.prefixes.map { it.name },
            containsInAnyOrder(tupIdsSortedbyTitle[1], tupIdsSortedbyTitle[2])
        )
        val title2Folder = storage.reference
            .child("images/$USER_NAME/tupperwares/${tupIdsSortedbyTitle[1]}").listAll().await()
        val title3Folder = storage.reference
            .child("images/$USER_NAME/tupperwares/${tupIdsSortedbyTitle[2]}").listAll().await()
        assertThat(title2Folder.items.count(), `is`(1))
        assertThat(title3Folder.items.count(), `is`(2))
    }

    @Test
    fun deleteRecipe() = runTest {
        val file = withContext(Dispatchers.IO) {
            generateTempFiles(2)
        }
        val urls = file.map { Uri.fromFile(it) }
        val tup = Tupperware("title1", "desc1", USER_NAME)
        tupperwareRepository.add(tup.title, tup.description, urls)
        val tupId = tupperwareRepository
            .getWithGivenField<Tupperware>("title", "${tup.title}").first().id
        runBlocking { tupperwareRepository.delete(tupId) }
        val tups = tupperwareRepository.getAll<Tupperware>()
        assert(tups.isEmpty())
        val images = storage.reference.child("images/$USER_NAME/tupperwares").listAll().await()
        assert(images.prefixes.isEmpty())
    }

    private fun generateTempFiles(count: Int): List<File> =
        (0 until count).map {
            val file = File.createTempFile("temp_", "$it")
            file.writeText("temp$it")
            file.deleteOnExit()
            file
        }

    // TODO: replace with tests for new functionality as part of #32: https://github.com/cook4me/android/issues/32
//    @Test
//    fun updateExistingTupperwareKeepOnlyRecentTupperware() = runTest {
//        val entryToBeUpdated = Tupperware(
//            title = "title", description = "desc", tags = listOf("Pizza", "Italian"), images = listOf("Uri")
//        )
//        tupperwareRepository.add(entryToBeUpdated)
//        val allTupperwareBeforeUpdate = tupperwareRepository.getAll<Tupperware>()
//        val updatedEntry = entryToBeUpdated.copy(title = "updated")
//        tupperwareRepository.update(allTupperwareBeforeUpdate.keys.first(), updatedEntry)
//        val allTupperwareAfterUpdate = tupperwareRepository.getAll<Tupperware>()
//        MatcherAssert.assertThat(allTupperwareAfterUpdate.values, Matchers.contains(updatedEntry))
//        MatcherAssert.assertThat(allTupperwareAfterUpdate.values, Matchers.not(Matchers.contains(entryToBeUpdated)))
//    }

//    @Test
//    fun getTupperwareById() = runTest {
//        tupperwareRepository.add(
//            Tupperware(
//                title = "title0", description = "desc0", tags = listOf("Pizza", "Hungarian"), images = listOf("Uri0")
//            )
//        )
//        tupperwareRepository.add(
//            Tupperware(
//                title = "title1", description = "desc1", tags = listOf("Langosh", "Italian"), images = listOf("Uri1")
//            )
//        )
//        tupperwareRepository.add(
//            Tupperware(
//                title = "title2", description = "desc2", tags = listOf("Langosh", "Hungarian"), images = listOf("Uri2")
//            )
//        )
//        val allTupperware = tupperwareRepository.getAll<Tupperware>()
//        val actual = tupperwareRepository.getById<Tupperware>(allTupperware.keys.first())
//        MatcherAssert.assertThat(actual, Matchers.`is`(allTupperware.values.first()))
//    }
}
