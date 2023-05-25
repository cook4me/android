package ch.epfl.sdp.cook4me.ui.event.details

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.event.testEvent
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailedEventScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val store: FirebaseFirestore = setupFirestore()
    private val eventRepository = EventRepository(store)
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var eventId: String

    @Before
    fun setUp() {
        runBlocking {
            eventId = eventRepository.add(testEvent)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            eventRepository.deleteAll()
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

        composeTestRule.onNodeWithStringId(R.string.event_creator).assertIsDisplayed()
        composeTestRule.onNodeWithText("peter griffin").performScrollTo()
        composeTestRule.onNodeWithText("peter griffin").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_max_participants).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_max_participants).assertIsDisplayed()
        composeTestRule.onNodeWithText("4").performScrollTo()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.event_time).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_time).assertIsDisplayed()
        composeTestRule.onNodeWithText(testEvent.eventDate).performScrollTo()
        composeTestRule.onNodeWithText(testEvent.eventDate).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.detailed_event_create_chat).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.detailed_event_create_chat).assertIsDisplayed()
    }

    @Test
    fun testDetailedEventScreenShowsLoadingScreen() {
        composeTestRule.setContent {
            DetailedEventScreen("somefakeid")
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(context.getString(R.string.Loading_Screen_Tag))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.Loading_Screen_Tag)).assertIsDisplayed()
    }
}
