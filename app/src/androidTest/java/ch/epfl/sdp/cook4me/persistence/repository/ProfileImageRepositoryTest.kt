package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.generateTempFiles
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val USER_NAME = "donald.duck@epfl.ch"
private const val PASSWORD = "123456"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProfileImageRepositoryTest {
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)
//
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
            profileImageRepository.deleteImageForCurrentUser()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun storeAndRetrieveNewProfileImage() = runTest {
        val file = generateTempFiles(1)
        val urls = file.map { Uri.fromFile(it) }
        val firebaseImageUri = profileImageRepository.add(image = urls.first())
        val profileImage = profileImageRepository.getProfile()
        assertThat(
            profileImage,
            `is`(firebaseImageUri)
        )
    }

    @Test
    fun deleteProfileImage() = runTest {
        val file = generateTempFiles(1)
        val url = file.map { Uri.fromFile(it) }
        profileImageRepository.add(url.first())
        profileImageRepository.deleteImageForCurrentUser()
        val userImage = profileImageRepository.getProfile()
        // Check if the userImage is the default image
        assert(userImage.toString() == "android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
        // Check if the image is deleted from the storage
        val images = storage.reference.child("images/$USER_NAME/profileImage").listAll().await()
        assertThat(images.prefixes.isEmpty(), `is`(true))
    }
}
