package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var firestore: FirebaseFirestore
    private val COLLECTION_PATH = "profiles"
    private val id = "harry.potter@epfl.ch"
    private val user = mapOf(
        "favoriteDish" to "Spaghetti",
        "allergies" to "Hazelnut",
        "bio" to "Gourmet",
        "id" to "harry.potter@epfl.ch",
        "name" to "Harry",
        "photos" to listOf<String>(""),
        "userImage" to "",
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        /*
        * IMPORTANT:
        * (Below code is already functional, no need to change anything)
        * Make sure you do this try-catch block,
        * otherwise when doing CI, there will be an exception:
        * kotlin.UninitializedPropertyAccessException: lateinit property firestore has not been initialized
        * */
        try {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        }
        runBlocking {
            firestore.collection(COLLECTION_PATH).document(id).set(user).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            firestore.collection(COLLECTION_PATH).document(id).delete().await()
        }
        runBlocking {
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun editScreenTest() {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)
        val favFood = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        composeTestRule.setContent { EditProfileScreen() }

        val usernameInput = "ronald"
        val favoriteDishInput = "Butterbeer"
        val allergiesInput = "Snails"
        val bioInput = "I'm just the friend of harry"

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favFood).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(username)
                .fetchSemanticsNodes().size == 1
        }

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favFood).performTextInput(favoriteDishInput)
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
    fun editProfileScreenStateTest() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent { EditProfileScreen(viewModel = profileViewModel) }

        profileViewModel.isLoading.value = true

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()

        profileViewModel.isLoading.value = false

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertDoesNotExist()
    }
}
