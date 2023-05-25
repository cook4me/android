package ch.epfl.sdp.cook4me.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.generateTempFiles
import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.persistence.repository.addMultipleTestTupperware
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
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
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val tupperwareRepository: TupperwareRepository =
        TupperwareRepository(store, storage, auth)
    private val swipeRepository: SwipeRepository = SwipeRepository(store, auth)
    private val swipeService: SwipeService = SwipeService(swipeRepository, auth, tupperwareRepository)

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_A, PASSWORD_A).await()
            auth.signOut()
            auth.createUserWithEmailAndPassword(USER_B, PASSWORD_B).await()
            auth.signOut()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            tupperwareRepository.deleteAll()
            swipeRepository.deleteAllByUser(USER_A)
            swipeRepository.deleteAllByUser(USER_B)
            auth.signOut()
            signInWithUserA()
            auth.currentUser?.delete()?.await()
            signInWithUserB()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun noMatchOnNoOverlap() = runTest {
        val filesA = generateTempFiles(5)
        val filesB = generateTempFiles(5)
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        auth.signOut()
        signInWithUserB()
        tupperwareRepository.addMultipleTestTupperware(filesB)
        auth.signOut()
        signInWithUserA()
        swipeService.getAllUnswipedTupperware().forEach {
            swipeService.storeSwipeResult(it.key, true)
            assertThat(swipeService.isMatch(it.key), `is`(false))
        }
    }

    @Test
    fun matchOnOverlap() = runTest {
        val filesA = generateTempFiles(5)
        val filesB = generateTempFiles(5)
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        auth.signOut()
        signInWithUserB()
        tupperwareRepository.addMultipleTestTupperware(filesB)
        swipeService.getAllUnswipedTupperware().forEach {
            swipeService.storeSwipeResult(it.key, true)
        }
        auth.signOut()
        signInWithUserA()
        swipeService.getAllUnswipedTupperware().forEach {
            swipeService.storeSwipeResult(it.key, true)
            assertThat(swipeService.isMatch(it.key), `is`(true))
        }
    }

    @Test
    fun getAllUnswipedTupperwareAfterSwipesTest() = runTest {
        val filesA = generateTempFiles(5)
        signInWithUserA()
        tupperwareRepository.addMultipleTestTupperware(filesA)
        auth.signOut()
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
