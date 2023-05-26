package ch.epfl.sdp.cook4me.ui.user.signup

import AddProfileInfoScreen
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val VALID_USER = "valid.user@epfl.ch"
private const val VALID_PASSWORD = "123456"

@RunWith(AndroidJUnit4::class)
class AddProfileTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val accountService = AccountService(auth)
    private val profileRepository = ProfileRepository(store)
    private val profileImageRepository = ProfileImageRepository(storage, auth)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val context: Context = getInstrumentation().targetContext

    @Test
    fun successfulSignUpAndAddProfileFlow() {
        var successAddProfileInfoCalled = false
        runBlocking {
            accountService.registerAndLogin(VALID_USER, VALID_PASSWORD)
        }
        composeTestRule.setContent {
            AddProfileInfoScreen(
                accountService = accountService,
                profileRepository = profileRepository,
                profileImageRepository = profileImageRepository,
                onAddingSuccess = { successAddProfileInfoCalled = true },
                onSkipClick = {}
            )
        }
        val name = "Name"
        val someDish = "Some dish"
        val allergies = "allergies"
        val bio = "bio"
        composeTestRule.onNodeWithText(context.getString(R.string.add_profile_name))
            .performTextInput(name)
        composeTestRule.onNodeWithText(context.getString(R.string.add_profile_favoriteDish))
            .performTextInput(someDish)
        composeTestRule.onNodeWithText(context.getString(R.string.add_profile_allergies))
            .performTextInput(allergies)
        composeTestRule.onNodeWithText(context.getString(R.string.add_profile_bio))
            .performTextInput(bio)
        composeTestRule.onNodeWithText(context.getString(R.string.add_profile_finish))
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            successAddProfileInfoCalled
        }
        val expected = Profile(
            email = VALID_USER,
            name = name,
            favoriteDish = someDish,
            allergies = allergies,
            bio = bio
        )
        runBlocking {
            val actual = profileRepository.getById(VALID_USER)
            assertThat(actual, `is`(expected))
            profileRepository.deleteAll()
            auth.currentUser?.delete()?.await()
        }
    }
}
