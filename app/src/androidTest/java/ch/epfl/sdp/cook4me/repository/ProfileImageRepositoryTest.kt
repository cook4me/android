package ch.epfl.sdp.cook4me.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
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
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

private const val COLLECTION_PATH = "profiles"
private const val USER_NAME = "donald.duck@epfl.ch"

@ExperimentalCoroutinesApi
class ProfileImageRepositoryTest {
    private lateinit var profileImageRepository: ProfileImageRepository
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
        profileImageRepository = ProfileImageRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, "123456").await()
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshAot in querySnapshot.documents) {
                profileImageRepository.delete()
            }
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeAndRetrieveNewProfileImage() = runTest {
        val file = withContext(Dispatchers.IO) {
            generateTempFiles(1)
        }
        val urls = file.map { Uri.fromFile(it) }
        val firebaseImageUri = profileImageRepository.add(image = urls.first())

        val profileImage = profileImageRepository.get()
        MatcherAssert.assertThat(
            profileImage,
            Matchers.`is`(firebaseImageUri)
        )
    }

    @Test
    fun deleteProfileImage() = runTest {
        val file = withContext(Dispatchers.IO) {
            generateTempFiles(1)
        }
        val url = file.map { Uri.fromFile(it) }
        profileImageRepository.add(url.first())

        runBlocking { profileImageRepository.delete() }
        val userImage = profileImageRepository.get()

        // Check if the userImage is the default image
        assert(userImage.toString() == "android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")

        // Check if the image is deleted from the storage
        val images = storage.reference.child("images/$USER_NAME/profileImage").listAll().await()
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
