package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import ch.epfl.sdp.cook4me.BuildConfig.MAPS_API_KEY
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import io.mockk.Ordering
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

private const val MAPS_LOADING_TIMEOUT = 8000.toLong()
private const val STARTING_ZOOM = 10f
private const val ASSERT_ROUNDING_ERROR = 0.01
private const val HALF_MINUTE_IN_MILLISECONDS = 20000L

class GoogleMapViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val startingPosition = Locations.LAUSANNE
    private lateinit var cameraPositionState: CameraPositionState
    private var navigatedToCreateEvent = false

    private fun initMap(content: @Composable () -> Unit = {}, selectedEventId: String = "") {
        check(hasValidApiKey()) { "Maps API key not specified" }
        val countDownLatch = CountDownLatch(1)
        composeTestRule.setContent {
            GoogleMapView(
                markers = dummyMarkers,
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
        assertTrue("Map loaded", mapLoaded)
    }

    @Before
    fun setUp() {
        cameraPositionState = spyk(
            CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    startingPosition,
                    STARTING_ZOOM
                )
            )
        )
    }

    @Test
    fun testEventInformationIsDisplayedWhenEventSelected() {
        initMap(selectedEventId = dummyMarkers[0].id)
        composeTestRule.onNodeWithText("Location: ${dummyMarkers[0].title}").assertIsDisplayed()
    }

    @Test
    fun testNoEventIsDisplayedWhenNoEventIsSelected() {
        initMap()
        composeTestRule.onNodeWithText("Select an event").assertIsDisplayed()
    }

    @Test
    fun testEventButtonClickNavigatesToEventScreen() {
        initMap(selectedEventId = dummyMarkers[0].id)
        composeTestRule.onNodeWithText("Explore event").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore event").performClick()
        composeTestRule.onNodeWithText("Navigate to event with id: ${dummyMarkers[0].id}").assertIsDisplayed()
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
        composeTestRule.onRoot().printToLog("DEBUG")
        composeTestRule.onNodeWithText("Create a new Event").performClick()
        assertTrue(navigatedToCreateEvent)
    }

    fun checkCameraPosition(nodeText: String, location: LatLng) {
        composeTestRule.onNodeWithText(nodeText).performClick()
        assertMoveHappened(cameraPositionState)
        location.assertEquals(cameraPositionState.position.target)
    }
}

private fun assertMoveHappened(cameraPositionState: CameraPositionState) {
    verify(ordering = Ordering.ORDERED, timeout = HALF_MINUTE_IN_MILLISECONDS) {
        cameraPositionState setProperty "isMoving" value true
        cameraPositionState setProperty "isMoving" value false
    }
}

private fun hasValidApiKey(): Boolean =
    MAPS_API_KEY.isNotBlank()

private fun LatLng.assertEquals(other: LatLng) {
    assertEquals(latitude, other.latitude, ASSERT_ROUNDING_ERROR)
    assertEquals(longitude, other.longitude, ASSERT_ROUNDING_ERROR)
}
