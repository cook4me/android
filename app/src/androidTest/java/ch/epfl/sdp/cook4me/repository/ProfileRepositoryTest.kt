package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

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

    // TODO: check flaky test
//    @Test
//    fun storeNewProfile() = runTest {
//        val newEntry1 = Profile(
//            email = "id1",
//            name = "megan",
//            bio = "super hot",
//            allergies = "turkey",
//            favoriteDish = "turkey",
//        )
//        val newEntry2 = Profile(
//            email = "id2",
//            name = "megan",
//            bio = "super hot",
//            allergies = "turkey",
//            favoriteDish = "turkey",
//        )
//        runBlocking {
//            // get the profile from the database
//            profileRepository.add(newEntry1)
//            profileRepository.add(newEntry2)
//            val profile1 = profileRepository.getById(newEntry1.email)
//            val profile2 = profileRepository.getById(newEntry2.email)
//            MatcherAssert.assertThat(profile1, Matchers.equalTo(newEntry1))
//            MatcherAssert.assertThat(profile2, Matchers.equalTo(newEntry2))
//            profileRepository.delete(newEntry1.email)
//            profileRepository.delete(newEntry2.email)
//        }
//    }

    // TODO: check flaky test
//    @Test
//    fun updateExistingProfile() = runTest {
//        val newEntry1 = Profile(
//            email = "id1",
//            name = "megan",
//            bio = "super hot",
//            allergies = "turkey",
//            favoriteDish = "turkey",
//        )
//
//        // get the profile from the database
//        runBlocking {
//            profileRepository.add(newEntry1)
//            val profile1 = profileRepository.getById(newEntry1.email)
//            profile1!!.name = "megan2.0"
//            profileRepository.update(profile1.email, profile1)
//            val profile2 = profileRepository.getById(profile1.email)
//            MatcherAssert.assertThat(profile2, Matchers.equalTo(profile1))
//            profileRepository.delete(newEntry1.email)
//        }
//    }
}
