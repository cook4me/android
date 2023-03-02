package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.WelcomeScreen
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun welcomeScreen_verifyContent() {
        composeTestRule.setContent {
            WelcomeScreen(
                onStartButtonClicked = {},
            )
        }

        composeTestRule.onNodeWithStringId(R.string.welcome_message).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).assertIsDisplayed()
    }

    @Test
    fun welcomeScreen_checkInputText() {
        var name = ""
        composeTestRule.setContent {
            WelcomeScreen(
                onStartButtonClicked = { name = it },
            )
        }

        composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field)
            .performTextInput("James Bond")
        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).performClick()
        assertThat(name, `is`("James Bond"))
    }
}