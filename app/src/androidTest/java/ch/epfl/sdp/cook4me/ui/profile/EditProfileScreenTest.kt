package ch.epfl.sdp.cook4me.ui.profile

import AddProfileInfoScreen
import EditProfileScreen
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("ronald.weasly@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("ronald.weasly@epfl.ch", "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("ronald.weasly@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun signUpScenario() = runTest {
        composeTestRule.setContent {
            AddProfileInfoScreen()
        }
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.tag_username)
        val favoriteDish = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        val usernameInput = "Harry"
        val favoriteDishInput = "Spaghetti"
        val allergiesInput = "Hazelnut"
        val bioInput = "Gourmet"

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favoriteDish).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favoriteDish).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()

        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()

        profile_test(usernameInput,favoriteDishInput,allergiesInput,bioInput,"")

        editScreen_test("ronald","Butterbeer", "Snails","I'm just the friend of harry", "")

        profile_test("ronald","Butterbeer", "Snails","I'm just the friend of harry", "")
    }

    private fun profile_test(usernameInput: String, favoriteDishInput: String, allergiesInput: String, bioInput: String, imageInput: String){
        composeTestRule.setContent { ProfileScreen() }

        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
        //TODO test image
    }

    private fun editScreen_test(usernameInput: String, favoriteDishInput: String, allergiesInput: String, bioInput: String, imageInput: String) {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.tag_username)
        val favoriteDish = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        composeTestRule.setContent { EditProfileScreen() }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favoriteDish).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favoriteDish).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()

        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
    }

    @Test
    fun clickingSaveButton() {
        composeTestRule.setContent {
            EditProfileScreen(ProfileCreationViewModel())
        }

        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
    }

    @Test
    fun clickingCancelButton() {
        composeTestRule.setContent {
            EditProfileScreen(ProfileCreationViewModel())
        }

        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
    }
}
