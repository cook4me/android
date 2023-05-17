package ch.epfl.sdp.cook4me.application

import addMultipleTestTupperware
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import generateTempFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val USER_A = "user.a@epfl.ch"
private const val PASSWORD_A = "password_a"

private const val USER_B = "user.b@epfl.ch"
private const val PASSWORD_B = "password_b"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SwipeServiceTest {

    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var tupperwareRepository: TupperwareRepository
    private lateinit var swipeRepository: SwipeRepository
    private lateinit var swipeService: SwipeService

    @Before
    fun setUp() {
        store = setupFirestore()
        storage = setupFirebaseStorage()
        auth = setupFirebaseAuth()
        tupperwareRepository = TupperwareRepository(store, storage, auth)
        swipeRepository = SwipeRepository(store, auth)
        swipeService = SwipeService(swipeRepository, auth, tupperwareRepository)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.createUserWithEmailAndPassword(USER_B, PASSWORD_B).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            tupperwareRepository.deleteAll()
            swipeRepository.deleteAll()
            signInWithUserA()
            auth.currentUser?.delete()
            signInWithUserB()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun noMatchOnNoOverlap() = runTest {
        val filesA = withContext(Dispatchers.IO) {
            generateTempFiles(5)
        }
        val filesB = withContext(Dispatchers.IO) {
            generateTempFiles(5)
        }
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        signInWithUserB()
        tupperwareRepository.addMultipleTestTupperware(filesB)
        signInWithUserA()
        swipeService.getAllUnswipedTupperware().forEach{
            swipeService.storeSwipeResult(it.key, true)
            assertThat(swipeService.isMatch(it.key), `is`(false))
        }
    }


    @Test
    fun matchOnOverlap() = runTest {
        val filesA = withContext(Dispatchers.IO) {
            generateTempFiles(5)
        }
        val filesB = withContext(Dispatchers.IO) {
            generateTempFiles(5)
        }
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        signInWithUserB()
        tupperwareRepository.addMultipleTestTupperware(filesB)
        swipeService.getAllUnswipedTupperware().forEach{
            swipeService.storeSwipeResult(it.key, true)
        }
        signInWithUserA()
        swipeService.getAllUnswipedTupperware().forEach{
            swipeService.storeSwipeResult(it.key, true)
            assertThat(swipeService.isMatch(it.key), `is`(true))
        }
    }

    @Test
    fun getAllUnswipedTupperwareAfterSwipesTest() = runTest {
        val filesA = withContext(Dispatchers.IO) {
            generateTempFiles(5)
        }
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        signInWithUserB()
        val tupperwareBefore = swipeService.getAllUnswipedTupperware()
        assertThat(tupperwareBefore.keys.count(), `is`(5))
        tupperwareBefore.keys.take(3).forEach {
            swipeService.storeSwipeResult(it, false)
        }
        val tupperwareAfter = swipeService.getAllUnswipedTupperware()
        assertThat(
            tupperwareAfter.keys,
            containsInAnyOrder(*tupperwareBefore.keys.drop(3).toTypedArray())
        )
    }

    private suspend fun signInWithUserA() =
        auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()

    private suspend fun signInWithUserB() =
        auth.signInWithEmailAndPassword(USER_B, PASSWORD_B).await()

}