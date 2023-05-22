package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.BuildConfig.MAPS_API_KEY
import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.event.testEvent
import com.google.android.gms.maps.model.CameraPosition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.CameraPositionState
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

private const val MAPS_LOADING_TIMEOUT = 8000.toLong()
private const val STARTING_ZOOM = 10f

@RunWith(AndroidJUnit4::class)
class GoogleMapViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val store: FirebaseFirestore = setupFirestore()
    private val eventRepository: EventRepository = EventRepository(store)

    private val startingPosition = Locations.LAUSANNE
    private lateinit var cameraPositionState: CameraPositionState
    private var navigatedToCreateEvent = false
    private lateinit var eventId: String

    @Before
    fun setUp() {
        runBlocking {
            eventId = eventRepository.add(testEvent)
        }
        cameraPositionState = spyk(
            CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    startingPosition,
                    STARTING_ZOOM
                )
            )
        )
    }

    @After
    fun cleanUp() {
        runBlocking {
            eventRepository.deleteAll()
        }
    }

    @Test
    fun testNoEventSelectedWhenStartingScreen() {
        initMap()
        // To be shown when no events are displayed
        composeTestRule.onNodeWithText("Select an event").assertIsDisplayed()
    }

    @Test
    fun testLatLngInVisibleRegion() {
        initMap()
        composeTestRule.runOnUiThread {
            val projection = cameraPositionState.projection
            assertNotNull(projection)
            assertTrue(
                projection!!.visibleRegion.latLngBounds.contains(startingPosition)
            )
        }
    }

    @Test
    fun testOnAddNewEventClick() {
        initMap()
        assertFalse(navigatedToCreateEvent)
        composeTestRule.onNodeWithText("Create a new Event").performClick()
        assertTrue(navigatedToCreateEvent)
    }

    private fun initMap(selectedEventId: String = "") {
        check(hasValidApiKey()) { "Maps API key not specified" }
        val countDownLatch = CountDownLatch(1)
        composeTestRule.setContent {
            GoogleMapView(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    countDownLatch.countDown()
                },
                selectedEventId = selectedEventId,
                onCreateNewEventClick = { navigatedToCreateEvent = true }
            )
        }

        val mapLoaded = countDownLatch.await(MAPS_LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
        assertTrue("Map wasn't loaded in $MAPS_LOADING_TIMEOUT ms", mapLoaded)
    }

    private fun hasValidApiKey(): Boolean =
        MAPS_API_KEY.isNotBlank()
}
