package ch.epfl.sdp.cook4me.repository

import addMultipleTestTupperware
import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.FirestoreTupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import generateTempFiles
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

@ExperimentalCoroutinesApi
class TupperwareRepositoryTest {
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var tupperwareRepository: TupperwareRepository

    @Before
    fun setUp() {
        store = setupFirestore()
        storage = setupFirebaseStorage()
        auth = setupFirebaseAuth()
        tupperwareRepository = TupperwareRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            tupperwareRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeAndGetNewTupperwareTest() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(2)
        }
        val ids = tupperwareRepository.addMultipleTestTupperware(files)
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
    fun getAllOwnIdsTest() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val expectedIds = tupperwareRepository.addMultipleTestTupperware(files)
        val actualIds = tupperwareRepository.getAllIdsByUser(
            auth.currentUser?.email ?: error("shouldn't happen")
        )
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }

    @Test
    fun getAllOtherIdsTest() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val expectedIds = tupperwareRepository.addMultipleTestTupperware(files)
        val actualIds = tupperwareRepository.getAllIdsNotByUser("other.user@epfl.ch")
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }

}