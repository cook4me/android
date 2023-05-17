package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val COLLECTION_PATH = "profiles"

@ExperimentalCoroutinesApi
class ProfileRepositoryTest {
    private lateinit var profileRepository: ProfileRepository
    private lateinit var store: FirebaseFirestore

    @Before
    fun setUp() {
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        profileRepository = ProfileRepository(store)
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshot in querySnapshot.documents) {
                store.collection(COLLECTION_PATH).document(documentSnapshot.id).delete().await()
            }
        }
    }

    @Test
    fun storeNewProfile() = runTest {
        val newEntry1 = Profile(
            email = "id1",
            name = "megan",
            bio = "super hot",
            allergies = "turkey",
            favoriteDish = "turkey",
        )
        val newEntry2 = Profile(
            email = "id2",
            name = "megan",
            bio = "super hot",
            allergies = "turkey",
            favoriteDish = "turkey",
        )
        profileRepository.add(newEntry1)
        profileRepository.add(newEntry2)
        val profile1 = profileRepository.getById(newEntry1.email)
        val profile2 = profileRepository.getById(newEntry2.email)
        MatcherAssert.assertThat(profile1, Matchers.equalTo(newEntry1))
        MatcherAssert.assertThat(profile2, Matchers.equalTo(newEntry2))
    }

    @Test
    fun updateExistingProfile() = runTest {
        val newEntry1 = Profile(
            email = "id1",
            name = "megan",
            bio = "super hot",
            allergies = "turkey",
            favoriteDish = "turkey",
        )

        // get the profile from the database
        profileRepository.add(newEntry1)
        val profile1 = profileRepository.getById(newEntry1.email)
        profile1!!.name = "megan2.0"
        profileRepository.update(profile1.email, profile1)
        val profile2 = profileRepository.getById(profile1.email)
        MatcherAssert.assertThat(profile2, Matchers.equalTo(profile1))
    }
}
