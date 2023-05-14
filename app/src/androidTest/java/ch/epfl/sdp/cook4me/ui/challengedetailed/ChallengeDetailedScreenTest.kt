package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.map.Locations.EPFL
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

private const val CHALLENGE_PATH = "challenges"
private const val FIREBASE_PORT = 8080
private const val AUTH_PORT = 9099

private const val MAIL_TEST = "peter.pan@epfl.ch"
private const val PWD_TEST = "123456"
@RunWith(AndroidJUnit4::class)
class ChallengeDetailedScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    private val challengeTest = Challenge(
        name = "Mountain Climbing",
        description = "Climb the highest peak of the city!",
        dateTime = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) },
        latLng = Pair(EPFL.latitude, EPFL.longitude),
        participants = mapOf("John" to 1, "Jane" to 2),
        creator = "Admin",
        type = "Spanish"
    )

    val challengeMap = createChallengeMap(challengeTest)
    var challengeId = ""

    @Before
    fun setUpChallenges() {
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
            val documentReference = firestore.collection(CHALLENGE_PATH).add(challengeMap).await()
            challengeId = documentReference.id
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

    fun createChallengeMap(challenge: Challenge): Map<String, Any> =
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
            ChallengeDetailView(challengeId = challengeId)
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
