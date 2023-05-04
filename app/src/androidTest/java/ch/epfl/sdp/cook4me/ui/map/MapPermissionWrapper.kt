package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import org.junit.Rule
import org.junit.Test

class MapPermissionWrapper {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assertMapIsDisplayedWhenLocationIsEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(true, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                markers = emptyList<MarkerData>(),
            )
        }

        composeTestRule.onNodeWithText("EPFL").assertIsDisplayed() // Map is shown
    }

    @Test
    fun assertMapIsNotDisplayedWhenLocationIsNotEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(false, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                markers = emptyList<MarkerData>(),
            )
        }

        composeTestRule.onNodeWithText("The location permission will grant a better experience in the app").assertIsDisplayed()
    }
}
