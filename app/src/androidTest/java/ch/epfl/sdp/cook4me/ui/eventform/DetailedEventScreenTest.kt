package ch.epfl.sdp.cook4me.ui.eventform

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class DetailedEventScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var context: Context
    private lateinit var testEvent: Event
    private val calendar = Calendar.getInstance()
    private lateinit var eventDate: String
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        calendar.set(Calendar.YEAR, 2023)
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
            id = "jabdsfias213",
            isPrivate = false
        )
        eventDate = testEvent.eventDate
    }
    @Test
    fun eventNameIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_name).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_name).assertIsDisplayed()
        composeTestRule.onNodeWithText("test event name").performScrollTo()
        composeTestRule.onNodeWithText("test event name").assertIsDisplayed()
    }

    @Test
    fun eventDescriptionIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_description).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_description).assertIsDisplayed()
        composeTestRule.onNodeWithText("test description").performScrollTo()
        composeTestRule.onNodeWithText("test description").assertIsDisplayed()
    }

    @Test
    fun eventLocationIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_location).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_location).assertIsDisplayed()
        composeTestRule.onNodeWithText("mondstadt").performScrollTo()
        composeTestRule.onNodeWithText("mondstadt").assertIsDisplayed()
    }

    @Test
    fun eventMaxParticipantsIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_max_participants).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_max_participants).assertIsDisplayed()
        composeTestRule.onNodeWithText("4").performScrollTo()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()
    }

    @Test
    fun eventCreatorIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_creator).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_creator).assertIsDisplayed()
        composeTestRule.onNodeWithText("peter griffin").performScrollTo()
        composeTestRule.onNodeWithText("peter griffin").assertIsDisplayed()
    }

    @Test
    fun eventPrivacyIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).assertIsDisplayed()
        composeTestRule.onNodeWithText("Everyone").performScrollTo()
        composeTestRule.onNodeWithText("Everyone").assertIsDisplayed()
    }

    @Test
    fun eventParticipantsIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        val testEventParticipants = testEvent.participants.joinToString(separator = ", ")
        composeTestRule.onNodeWithStringId(R.string.event_participants).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_participants).assertIsDisplayed()
        composeTestRule.onNodeWithText(testEventParticipants).performScrollTo()
        composeTestRule.onNodeWithText(testEventParticipants).assertIsDisplayed()
    }
    @Test
    fun eventDateTimeIsDisplayed() {
        composeTestRule.setContent {
            DetailedEventScreen(event = testEvent)
        }
        composeTestRule.onNodeWithStringId(R.string.event_time).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.event_time).assertIsDisplayed()
        composeTestRule.onNodeWithText(eventDate).performScrollTo()
        composeTestRule.onNodeWithText(eventDate).assertIsDisplayed()
    }
}
