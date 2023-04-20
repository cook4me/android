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
            initialPermissions = mapOf("TestPermission1" to Pair(true, true), "TestPermission2" to Pair(true, true))
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
            initialPermissions = mapOf("TestPermission1" to Pair(false, true), "TestPermission2" to Pair(false, true))
        )
        val permissionManager = PermissionManager(testPermissionStatusProvider)

        composeTestRule.setContent {
            permissionManager.withPermission {
                Text("Content with permissions")
            }
        }

        composeTestRule.onNodeWithText("Request permissions").assertExists()
        composeTestRule.onNodeWithText("The following permissions are important: TestPermission1, TestPermission2. Please grant all of them for the app to function properly.").assertExists()
    }

    @Test
    fun whenRequestPermissionsButtonClickedRequestAllPermissions() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            initialPermissions = mapOf("TestPermission1" to Pair(false, true), "TestPermission2" to Pair(false, true))
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

    @Test
    fun testPermissionRequestScreen_granted() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(true, true), "location" to Pair(true, true))
        )

        composeTestRule.setContent {
            PermissionRequestScreen(testPermissionStatusProvider)
        }

        composeTestRule.onNodeWithText("Camera and location permission Granted").assertIsDisplayed()
    }

    @Test
    fun testPermissionRequestScreen_revoked() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(false, true), "location" to Pair(false, true))
        )

        composeTestRule.setContent {
            PermissionRequestScreen(testPermissionStatusProvider)
        }

        composeTestRule.onNodeWithText("Camera and location permission Granted").assertDoesNotExist()
    }

    @Test
    fun testPermissionManager_permissionDenied_withoutRationale() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(false, false), "location" to Pair(false, false))
        )

        composeTestRule.setContent {
            PermissionManager(testPermissionStatusProvider).withPermission {
                Text("Camera and location permission Granted")
            }
        }

        val expectedText = "The following permissions will grant a better experience in the app: camera, location."
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun testPermissionManager_permissionDenied_withRationale() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(false, true), "location" to Pair(false, true))
        )

        composeTestRule.setContent {
            PermissionManager(testPermissionStatusProvider).withPermission {
                Text("Camera and location permission Granted")
            }
        }

        val expectedText = "The following permissions are important: camera, location. Please grant all of them for the app to function properly."
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun testPermissionManager_singularPermissionDenied_withoutRationale() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("location" to Pair(false, false))
        )

        composeTestRule.setContent {
            PermissionManager(testPermissionStatusProvider).withPermission {
                Text("Camera and location permission Granted")
            }
        }

        val expectedText = "The location permission will grant a better experience in the app"
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun testPermissionManager_singularPermissionDenied_withRationale() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(false, true))
        )

        composeTestRule.setContent {
            PermissionManager(testPermissionStatusProvider).withPermission {
                Text("Camera and location permission Granted")
            }
        }

        val expectedText = "The camera permission is important. Please grant it for the app to function properly."
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun testPermissionManager_permissionRequestButton() {
        val testPermissionStatusProvider = TestPermissionStatusProvider(
            mapOf("camera" to Pair(false, true), "location" to Pair(false, true))
        )

        composeTestRule.setContent {
            PermissionManager(testPermissionStatusProvider).withPermission {
                Text("Camera and location permission Granted")
            }
        }

        composeTestRule.onNodeWithText("Request permissions").assertIsDisplayed()
    }
}
