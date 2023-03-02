package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WelcomeFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun profileScreen_verifyContent() {
        composeTestRule.setContent {
            Cook4MeApp()
        }

        composeTestRule.onNodeWithStringId(R.string.welcome_message).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field)
            .performTextInput("James Bond")
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).performClick()
        composeTestRule.onNodeWithText("${composeTestRule.activity.getString(R.string.profile_screen_placeholder)} James Bond")
            .assertIsDisplayed()
    }
}