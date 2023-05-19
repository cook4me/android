package ch.epfl.sdp.cook4me.ui.challengedetailed

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.ui.detailedevent.cleanUpEvents
import ch.epfl.sdp.cook4me.ui.detailedevent.setUpEvents
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val CHALLENGE_PATH = "challenges"
@RunWith(AndroidJUnit4::class)
class ChallengeDetailedScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var context: Context
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var challengeId: String

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val (auth, firestore, challengeId) = setUpEvents(CHALLENGE_PATH)
        this.auth = auth
        this.firestore = firestore
        this.challengeId = challengeId
    }

    @After
    fun cleanUp() {
        cleanUpEvents(auth, firestore, challengeId, CHALLENGE_PATH)
    }

    @Test
    fun testChallengeDetailedScreen() {
        composeTestRule.setContent {
            ChallengeDetailedScreen(
                challengeId = challengeId,
                onVote = {},
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Join")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Mountain Climbing").assertIsDisplayed()
        composeTestRule.onNodeWithText("Climb the highest peak of the city!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time remaining: ").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Participants").assertIsDisplayed()
        composeTestRule.onNodeWithText("John").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jane").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Creator: Admin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type: Spanish").assertIsDisplayed()
        composeTestRule.onNodeWithText("Join").assertIsDisplayed()
    }

    @Test
    fun currentUserSuccessWhenJoiningEvent() {
        composeTestRule.setContent {
            ChallengeDetailedScreen(
                challengeId = challengeId,
                onVote = {},
            )
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Join")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Join").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("You have joined the challenge!")
                .fetchSemanticsNodes().size == 1
        }

        // Assert user has been added and UI is updated
        composeTestRule.onNodeWithText("harry.potter@epfl.ch").assertIsDisplayed()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vote for other participants").assertIsDisplayed()
    }
}
