package ch.epfl.sdp.cook4me.ui.detailedevent

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.event.details.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.Event
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
import org.junit.runner.RunWith
import java.util.Calendar

private const val EVENT_PATH = "events"
@RunWith(AndroidJUnit4::class)
class DetailedEventScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var context: Context
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val calendar = Calendar.getInstance()
    private lateinit var testEvent: Event
    private lateinit var eventDate: String
    private lateinit var eventId: String

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
        calendar.set(Calendar.YEAR, 2200)
        calendar.set(Calendar.MONTH, Calendar.MARCH)
        calendar.set(Calendar.DAY_OF_MONTH, 27)
        calendar.set(Calendar.HOUR_OF_DAY, 14)
        calendar.set(Calendar.MINUTE, 12)
        testEvent = Event(
            name = "test event name",
            description = "test description",
            dateTime = calendar,
            location = "mondstadt",
            maxParticipants = 4,
            participants = listOf("obi.wang", "harry.potter"),
            creator = "peter griffin",
            id = "harry.potter@epfl.ch",
            isPrivate = false
        )
        val eventMap = mapOf(
            "name" to testEvent.name,
            "description" to testEvent.description,
            "dateTime" to testEvent.dateTime,
            "location" to testEvent.location,
            "maxParticipants" to testEvent.maxParticipants,
            "participants" to testEvent.participants,
            "creator" to testEvent.creator,
            "id" to testEvent.id,
            "isPrivate" to testEvent.isPrivate
        )
        eventDate = testEvent.eventDate
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        }
        runBlocking {
            val documentReference = firestore.collection(EVENT_PATH).add(eventMap).await()
            eventId = documentReference.id
        }
    }
    @After
    fun cleanUp() {
        runBlocking {
            firestore.collection(EVENT_PATH).document(eventId).delete().await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun testCorrectDetailedEventInfoIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(eventId)
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("test event name")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithStringId(R.string.event_name).assertIsDisplayed()
        composeTestRule.onNodeWithText("test event name").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_description).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_description).assertIsDisplayed()
        composeTestRule.onNodeWithText("test description").performScrollTo()
        composeTestRule.onNodeWithText("test description").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_location).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_location).assertIsDisplayed()
        composeTestRule.onNodeWithText("mondstadt").performScrollTo()
        composeTestRule.onNodeWithText("mondstadt").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_location).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_creator).assertIsDisplayed()
        composeTestRule.onNodeWithText("peter griffin").performScrollTo()
        composeTestRule.onNodeWithText("peter griffin").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).assertIsDisplayed()
        composeTestRule.onNodeWithText("Everyone").performScrollTo()
        composeTestRule.onNodeWithText("Everyone").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_max_participants).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_max_participants).assertIsDisplayed()
        composeTestRule.onNodeWithText("4").performScrollTo()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()

        val testEventParticipants = testEvent.participants.joinToString(separator = ", ")
        composeTestRule.onNodeWithStringId(R.string.event_participants).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_participants).assertIsDisplayed()
        composeTestRule.onNodeWithText(testEventParticipants).performScrollTo()
        composeTestRule.onNodeWithText(testEventParticipants).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_time).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_time).assertIsDisplayed()
        composeTestRule.onNodeWithText(eventDate).performScrollTo()
        composeTestRule.onNodeWithText(eventDate).assertIsDisplayed()
    }
}
