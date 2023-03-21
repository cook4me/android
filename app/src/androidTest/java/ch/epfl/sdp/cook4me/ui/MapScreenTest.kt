package ch.epfl.sdp.cook4me.ui

import android.icu.util.UniversalTimeScale.toLong
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import ch.epfl.sdp.cook4me.BuildConfig.MAPS_API_KEY
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.Locations
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import ch.epfl.sdp.cook4me.R

private const val MAPS_MOVING_TIMEOUT = 1000.toLong()
private const val MAPS_LOADING_TIMEOUT = 5000.toLong()
class GoogleMapViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val startingZoom = 10f
    private val startingPosition = Locations.LAUSANNE
    private lateinit var cameraPositionState: CameraPositionState


    private fun initMap(content: @Composable () -> Unit = {}) {
        check(hasValidApiKey) { "Maps API key not specified" }
        val countDownLatch = CountDownLatch(1)
        composeTestRule.setContent {
            GoogleMapView(
                markers = dummyMarkers,
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    countDownLatch.countDown()
                }
            )
        }
        val mapLoaded = countDownLatch.await(MAPS_LOADING_TIMEOUT, TimeUnit.SECONDS)
        assertTrue("Map loaded", mapLoaded)
    }

    @Before
    fun setUp() {
        cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                startingPosition,
                startingZoom
            )
        )
    }

    @Test
    fun testUniversitiesButtonsSetCameraPosition() {
        fun checkCameraPosition(name: String, location: LatLng) {
            composeTestRule.onNodeWithText(name).performClick()
            composeTestRule.waitUntil(MAPS_MOVING_TIMEOUT) {
                cameraPositionState.isMoving
            }
            composeTestRule.waitUntil(MAPS_LOADING_TIMEOUT) {
                !cameraPositionState.isMoving
            }
            location.assertEquals(cameraPositionState.position.target)
        }

        initMap()
        checkCameraPosition(name = "EPFL", Locations.EPFL)
        checkCameraPosition(name = "UNIL", Locations.UNIL)
    }

    @Test
    fun testStartingCameraPosition() {
        initMap()
        startingPosition.assertEquals(cameraPositionState.position.target)
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
}

const val assertRoundingError: Double = 0.01

val hasValidApiKey: Boolean =
    MAPS_API_KEY.isNotBlank()

fun LatLng.assertEquals(other: LatLng) {
    assertEquals(latitude, other.latitude, assertRoundingError)
    assertEquals(longitude, other.longitude, assertRoundingError)
}

fun LatLng.assertNotEquals(other: LatLng) {
    assertNotEquals(latitude, other.latitude, assertRoundingError)
    assertNotEquals(longitude, other.longitude, assertRoundingError)
}
