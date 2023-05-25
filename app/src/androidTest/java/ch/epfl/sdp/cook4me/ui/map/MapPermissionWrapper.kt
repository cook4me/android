package ch.epfl.sdp.cook4me.ui.map

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.TestPermissionStatusProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestMapPermissionWrapper {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun assertMapIsDisplayedWhenLocationIsEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(true, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                isOnline = true,
            )
        }

        composeTestRule.onNodeWithText("Create a new Event").assertIsDisplayed() // Map is shown
    }

    @Test
    fun assertMapIsNotDisplayedWhenLocationIsNotEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(false, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                isOnline = true,
            )
        }

        composeTestRule.onNodeWithText("The Location permission will grant a better experience in the app").assertIsDisplayed()
    }
}
