package ch.epfl.sdp.cook4me.ui.tupperwareform

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateTupperwarePermissionWrapperTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assertCreateTupperwareIsDisplayedWhenCameraIsEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("camera" to Pair(true, false)))

        composeTestRule.setContent {
            CreateTupperwarePermissionWrapper(permissionStatusProvider = permissionStatusProvider, {}, {})
        }

        composeTestRule.onNodeWithText(text = "Description").assertIsDisplayed()
    }

    @Test
    fun assertCreateTupperwareIsNotDisplayedWhenCameraIsNotEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("camera" to Pair(false, false)))

        composeTestRule.setContent {
            CreateTupperwarePermissionWrapper(permissionStatusProvider = permissionStatusProvider, {}, {})
        }

        composeTestRule.onNodeWithText("The camera permission will grant a better experience in the app").assertIsDisplayed()
    }
}
