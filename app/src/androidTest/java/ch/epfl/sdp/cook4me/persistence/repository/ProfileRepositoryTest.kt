package ch.epfl.sdp.cook4me.persistence.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProfileRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val profileRepository = ProfileRepository(store)

    @After
    fun cleanup() {
        runBlocking {
            profileRepository.deleteAll()
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
        // get the profile from the database
        profileRepository.add(newEntry1)
        profileRepository.add(newEntry2)
        val profile1 = profileRepository.getById(newEntry1.email)
        val profile2 = profileRepository.getById(newEntry2.email)
        assertThat(profile1, `is`(newEntry1))
        assertThat(profile2, `is`(newEntry2))
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

        profileRepository.add(newEntry1)
        val profile1 = profileRepository.getById(newEntry1.email)
        profile1!!.name = "megan2.0"
        profileRepository.update(profile1.email, profile1)
        val profile2 = profileRepository.getById(profile1.email)
        assertThat(profile2, `is`(profile1))
    }
}
