package ch.epfl.sdp.cook4me.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.FirestoreTupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.mockk.InternalPlatformDsl.toArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

private const val USER_A = "user.a@epfl.ch"
private const val PASSWORD_A = "password_a"

private const val USER_B = "user.b@epfl.ch"
private const val PASSWORD_B = "password_b"

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
            auth.createUserWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.createUserWithEmailAndPassword(USER_B, PASSWORD_B).await()
            auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            tupperwareRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.currentUser?.delete()
            auth.signInWithEmailAndPassword(USER_B, PASSWORD_B).await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeAndGetNewTupperware() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(2)
        }
        val ids = addMultipleTupperware(files)
        ids.zip(files).forEachIndexed { i, data ->
            val actual = tupperwareRepository.getWithImageById(data.first)
            assertThat(actual, `is`(notNullValue()))
            actual?.let {
                assertThat(it.title, `is`("title$i"))
                assertThat(it.description, `is`("desc$i"))
                assertThat(it.user, `is`(auth.currentUser?.email))
                assertThat(it.image, `is`(data.second.readBytes()))
            }
        }
    }

    @Test
    fun getAllOwnIds() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val expectedIds = addMultipleTupperware(files)
        val actualIds = tupperwareRepository.getAllIdsByUser(USER_A)
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }

    @Test
    fun getAllOtherIds() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val expectedIds = addMultipleTupperware(files)
        val actualIds = tupperwareRepository.getAllIdsNotByUser(USER_B)
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }

    private suspend fun addMultipleTupperware(files: List<File>) =
        files.mapIndexed { i, file ->
            tupperwareRepository.add(
                "title$i",
                "desc$i",
                Uri.fromFile(file)
            )
        }
}

private fun generateTempFiles(count: Int): List<File> =
    (0 until count).map {
        val file = File.createTempFile("temp_", "$it")
        file.writeText("temp$it")
        file.deleteOnExit()
        file
    }



