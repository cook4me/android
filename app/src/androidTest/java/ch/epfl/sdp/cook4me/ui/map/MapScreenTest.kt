package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.type.LatLng
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val MAPS_LOADING_TIMEOUT = 8000.toLong()
private const val STARTING_ZOOM = 10f
private const val ASSERT_ROUNDING_ERROR = 0.01
private const val HALF_MINUTE_IN_MILLISECONDS = 20000L


@RunWith(AndroidJUnit4::class)
class GoogleMapViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()
//
//    private val startingPosition = Locations.LAUSANNE
//    private lateinit var cameraPositionState: CameraPositionState
//    private var navigatedToCreateEvent = false
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var eventId: String
//
//    private fun initMap(selectedEventId: String = "") {
//        check(hasValidApiKey()) { "Maps API key not specified" }
//        val countDownLatch = CountDownLatch(1)
//        composeTestRule.setContent {
//            GoogleMapView(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState,
//                onMapLoaded = {
//                    countDownLatch.countDown()
//                },
//                selectedEventId = selectedEventId,
//                onCreateNewEventClick = { navigatedToCreateEvent = true }
//            )
//        }
//
//        val mapLoaded = countDownLatch.await(MAPS_LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
//        assertTrue("Map loaded", mapLoaded)
//    }

//    @Before
//    fun setUp() {
//        val (auth, firestore, eventId) = setUpEvents()
//        this.auth = auth
//        this.firestore = firestore
//        this.eventId = eventId
//        cameraPositionState = spyk(
//            CameraPositionState(
//                position = CameraPosition.fromLatLngZoom(
//                    startingPosition,
//                    STARTING_ZOOM
//                )
//            )
//        )
//    }

//    @After
//    fun cleanUp() {
//        cleanUpEvents(auth, firestore, eventId)
//    }

//    @Test
//    fun testEventInformationIsDisplayedWhenEventSelected() {
//        initMap(selectedEventId = eventId)
//        // composeTestRule.onNodeWithText("Location: test event name").assertIsDisplayed()
//    }

//    @Test
//    fun testNoEventIsDisplayedWhenNoEventIsSelected() {
//        initMap()
//        composeTestRule.onNodeWithText("Select an event").assertIsDisplayed()
//    }

//    @Test
//    fun testEventButtonClickNavigatesToEventScreen() {
//        initMap(selectedEventId = eventId)
//    /*
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("Explore event")
//                .fetchSemanticsNodes().size == 1
//        }
//
//        composeTestRule.onNodeWithText("Explore event").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Explore event").performClick()
//        */
//    }

//    @Test
//    fun testNoEventSelectedWhenStartingScreen() {
//        initMap()
//        // To be shown when no events are displayed
//        composeTestRule.onNodeWithText("Select an event").assertIsDisplayed()
//    }

//    @Test
//    fun testLatLngInVisibleRegion() {
//        initMap()
//        composeTestRule.runOnUiThread {
//            val projection = cameraPositionState.projection
//            assertNotNull(projection)
//            assertTrue(
//                projection!!.visibleRegion.latLngBounds.contains(startingPosition)
//            )
//        }
//    }

    @Test
    fun testOnAddNewEventClick() {
        initMap()
        assertFalse(navigatedToCreateEvent)
        composeTestRule.onNodeWithText("Create a new Event").performClick()
        assertTrue(navigatedToCreateEvent)
    }

//    fun checkCameraPosition(nodeText: String, location: LatLng) {
//        composeTestRule.onNodeWithText(nodeText).performClick()
//        assertMoveHappened(cameraPositionState)
//        location.assertEquals(cameraPositionState.position.target)
//    }
// }
//
// private fun assertMoveHappened(cameraPositionState: CameraPositionState) {
//    verify(ordering = Ordering.ORDERED, timeout = HALF_MINUTE_IN_MILLISECONDS) {
//        cameraPositionState setProperty "isMoving" value true
//        cameraPositionState setProperty "isMoving" value false
//    }
}

//
// private fun hasValidApiKey(): Boolean =
//    MAPS_API_KEY.isNotBlank()
//
private fun LatLng.assertEquals(other: LatLng) {
    assertEquals(latitude, other.latitude, ASSERT_ROUNDING_ERROR)
    assertEquals(longitude, other.longitude, ASSERT_ROUNDING_ERROR)
}
