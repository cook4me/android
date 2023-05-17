package ch.epfl.sdp.cook4me.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.FirestoreTupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
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

private const val USER_NAME = "harry.potter@epfl.ch"
private const val PASSWORD = "WingardiumLeviosa"

@ExperimentalCoroutinesApi
class TupperwareRepositoryTest {
    private lateinit var tupperwareRepository: TupperwareRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        store = setupFirestore()
        storage = setupFirebaseStorage()
        auth = setupFirebaseAuth()
        tupperwareRepository = TupperwareRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            tupperwareRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeNewTupperware() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(2)
        }
        val urls = files.map { Uri.fromFile(it) }
        tupperwareRepository.add(title = "title1", description = "desc1", image = null)
        tupperwareRepository.add(
            title = "title2",
            description = "desc2",
            image = urls[0]
        )
        tupperwareRepository.add(
            title = "title3",
            description = "desc3",
            image = urls[1]
        )
        val allTupperware = tupperwareRepository.getAll<FirestoreTupperware>()
        assertThat(
            allTupperware.values,
            containsInAnyOrder(
                FirestoreTupperware("title1", "desc1", USER_NAME),
                FirestoreTupperware("title2", "desc2", USER_NAME),
                FirestoreTupperware("title3", "desc3", USER_NAME),
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

    //TODO: ????
    @Test
    fun deleteRecipe() = runTest {
        val file = withContext(Dispatchers.IO) {
            generateTempFiles(1)
        }
        val urls = file.map { Uri.fromFile(it) }
        val tup = FirestoreTupperware("title1", "desc1", USER_NAME)
        tupperwareRepository.add(tup.title, tup.description, urls.first())
        val tupId = tupperwareRepository
            .getWithGivenField<FirestoreTupperware>("title", tup.title).first().id
        runBlocking { tupperwareRepository.delete(tupId) }
        val tups = tupperwareRepository.getAll<FirestoreTupperware>()
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
}
