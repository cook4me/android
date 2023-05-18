package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val USER_NAME = "john.snow@epfl.ch"
private const val PASSWORD = "ygritte"
@ExperimentalCoroutinesApi
class SwipeRepositoryTest {
    private lateinit var swipeRepository: SwipeRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        store = setupFirestore()
        auth = setupFirebaseAuth()
        swipeRepository = SwipeRepository(store, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            swipeRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun addIdsAndGetThemTest() = runTest {
        swipeRepository.add("tup1", false)
        swipeRepository.add("tup2", true)
        swipeRepository.add("tup3", true)
        val ids = swipeRepository.getAllIdsByUser(auth.currentUser?.email ?: error("shouldn't happen"))
        assertThat(ids, containsInAnyOrder("tup1", "tup2", "tup3"))
    }

    @Test
    fun getAllPositiveIdsTest() = runTest {
        swipeRepository.add("id1", false)
        swipeRepository.add("id2", true)
        swipeRepository.add("id3", true)
        swipeRepository.add("id4", false)
        val ids = swipeRepository.getAllPositiveIdsByUser(auth.currentUser?.email ?: error("shouldn't happen"))
        assertThat(ids, containsInAnyOrder("id2", "id3"))
    }
}
