package ch.epfl.sdp.cook4me.ui.challengedetailed

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.detailedevent.cleanUpEvents
import ch.epfl.sdp.cook4me.ui.detailedevent.setUpEvents
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
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

    /*
    fun setUpChallenges() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        auth = FirebaseAuth.getInstance()
        auth.useEmulator("10.0.2.2", AUTH_PORT)

        firestore = FirebaseFirestore.getInstance()
        firestore.useEmulator("10.0.2.2", FIREBASE_PORT)

        // Sign in
        runBlocking {
            try {
                auth.signInWithEmailAndPassword(MAIL_TEST, PWD_TEST).await()
            } catch (e: FirebaseException) {
                auth.createUserWithEmailAndPassword(MAIL_TEST, PWD_TEST).await()
                auth.signInWithEmailAndPassword(MAIL_TEST, PWD_TEST).await()
            }
        }

        // Add challenge to Firestore
        runBlocking {
            try {
                val documentReference = firestore.collection(CHALLENGE_PATH).add(challengeMap).await()
                challengeId = documentReference.id
            } catch (ex: FirebaseException) {
                Log.e("Error when adding challenge", ex.toString())
            }
        }
    }

    @After
    fun cleanUpChallenges() {
        runBlocking {
            firestore.collection(CHALLENGE_PATH).document(challengeId).delete().await()
            auth.signInWithEmailAndPassword(MAIL_TEST, PWD_TEST).await()
            auth.currentUser?.delete()?.await()
        }
    }
    */
    private fun createChallengeMap(challenge: Challenge): Map<String, Any> =
        mapOf(
            "name" to challenge.name,
            "description" to challenge.description,
            "dateTime" to challenge.dateTime,
            "latLng" to GeoPoint(challenge.latLng.first, challenge.latLng.second),
            "participants" to challenge.participants,
            "creator" to challenge.creator,
            "type" to challenge.type
        )

    @Test
    fun testChallengeDetailedScreen() {
        composeTestRule.setContent {
            ChallengeDetailedScreen(challengeId = challengeId)
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
}
