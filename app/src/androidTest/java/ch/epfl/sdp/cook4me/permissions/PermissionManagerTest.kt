package ch.epfl.sdp.cook4me.permissions

import androidx.compose.material.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionManagerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenAllPermissionsGrantedContentIsDisplayed() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            initialPermissions = mapOf("TestPermission1" to true, "TestPermission2" to true)
        )
        val permissionManager = PermissionManager(testPermissionStatusProvider)

        composeTestRule.setContent {
            permissionManager.withPermission {
                Text("Content with permissions")
            }
        }

        composeTestRule.onNodeWithText("Content with permissions").assertExists()
    }

    @Test
    fun whenPermissionsDeniedPermissionRequestUIIsDisplayed() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            initialPermissions = mapOf("TestPermission1" to false, "TestPermission2" to false)
        )
        val permissionManager = PermissionManager(testPermissionStatusProvider)

        composeTestRule.setContent {
            permissionManager.withPermission {
                Text("Content with permissions")
            }
        }

        composeTestRule.onNodeWithText("Request permissions").assertExists()
        composeTestRule.onNodeWithText("The TestPermission1, and TestPermission2  permissions is important. Please grant all of them for the app to function properly.").assertExists()
    }

    @Test
    fun whenRequestPermissionsButtonClickedRequestAllPermissions() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            initialPermissions = mapOf("TestPermission1" to false, "TestPermission2" to false)
        )
        val permissionManager = PermissionManager(testPermissionStatusProvider)

        composeTestRule.setContent {
            permissionManager.withPermission {
                Text("Content with permissions")
            }
        }

        composeTestRule.onNodeWithText("Request permissions").performClick()

        // User gives permission in popped Android request
        testPermissionStatusProvider.setPermissionValue("TestPermission1", true)
        testPermissionStatusProvider.setPermissionValue("TestPermission2", true)

        composeTestRule.onRoot().printToLog("DEBUG")
        composeTestRule.onNodeWithText("Content with permissions").assertIsDisplayed()
    }
}
