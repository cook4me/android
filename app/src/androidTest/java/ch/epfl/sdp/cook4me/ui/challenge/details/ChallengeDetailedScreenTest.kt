package ch.epfl.sdp.cook4me.ui.challenge.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.repository.ChallengeRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.challenge.testChallenge
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "harry.potter@epfl.ch"
private const val PASSWORD = "123456"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ChallengeDetailedScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val store: FirebaseFirestore = setupFirestore()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val challengeRepository = ChallengeRepository(store)
    private lateinit var challengeId: String

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            challengeId = challengeRepository.add(testChallenge)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            challengeRepository.deleteAll()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
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

    @Test
    fun ifAlreadyVotedDisplaySeeResultsButton(){
        val votedChallenge = testChallenge.copy(participants = mapOf("John" to 1, "Jane" to 2, USERNAME to 0))
            .copy(participantIsVoted = mapOf(USERNAME to true))
        runBlocking {
            challengeId = challengeRepository.add(votedChallenge)
        }
        composeTestRule.setContent {
            ChallengeDetailedScreen(
                challengeId = challengeId,
                onVote = {},
            )
        }

        composeTestRule.onNodeWithStringId(R.string.see_votes).assertIsDisplayed()
    }
}
