package ch.epfl.sdp.cook4me.ui.challenge

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChallengeFeedScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testUIIsDiaplayed() {
        composeTestRule.setContent {
            ChallengeFeedScreen()
        }
        composeTestRule.onNodeWithTag("Challenge Feed TAG").assertIsDisplayed()
    }
}
