package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupperwareSwipeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun placeholderScreenIsShown() {
        composeTestRule.setContent {
            TupperwareSwipeScreen()
        }
        composeTestRule.onNodeWithText("Tupperware").assertIsDisplayed()
    }
}
