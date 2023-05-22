package ch.epfl.sdp.cook4me.ui.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestMapPermissionWrapper {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

// TODO: Fix the tests (potentially related to the network permission), https://github.com/cook4me/android/issues/250
//
//    @Test
//    fun assertMapIsDisplayedWhenLocationIsEnabled() {
//        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(true, false)))
//
//        composeTestRule.setContent {
//            MapPermissionWrapper(
//                permissionStatusProvider = permissionStatusProvider,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//
//        composeTestRule.onNodeWithText("EPFL").assertIsDisplayed() // Map is shown
//    }
//
//    @Test
//    fun assertMapIsNotDisplayedWhenLocationIsNotEnabled() {
//        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(false, false)))
//
//        composeTestRule.setContent {
//            MapPermissionWrapper(
//                permissionStatusProvider = permissionStatusProvider,
//                modifier = Modifier.fillMaxSize(),
//            )
//        }
//
//        composeTestRule.onNodeWithText("The location permission will grant a better experience in the app").assertIsDisplayed()
//    }
}
