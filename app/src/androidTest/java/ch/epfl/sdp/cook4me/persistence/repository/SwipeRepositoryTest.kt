package ch.epfl.sdp.cook4me.persistence.repository

 import androidx.test.ext.junit.runners.AndroidJUnit4
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
 import org.junit.runner.RunWith

 private const val USER_NAME = "john.snow@epfl.ch"
 private const val PASSWORD = "ygritte"

 @ExperimentalCoroutinesApi
 @RunWith(AndroidJUnit4::class)
 class SwipeRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val swipeRepository: SwipeRepository = SwipeRepository(store, auth)

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            swipeRepository.deleteAllByUser(USER_NAME)
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun addIdsAndGetThemTest() = runTest {
        swipeRepository.add("id1", false)
        swipeRepository.add("id2", true)
        swipeRepository.add("id3", true)
        val ids = swipeRepository.getAllIdsByUser(auth.currentUser?.email ?: error("shouldn't happen"))
        assertThat(ids, containsInAnyOrder("id1", "id2", "id3"))
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
