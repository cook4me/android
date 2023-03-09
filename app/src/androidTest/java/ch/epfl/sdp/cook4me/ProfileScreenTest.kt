package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import ch.epfl.sdp.cook4me.ui.ProfileScreen
import org.junit.Rule
import org.junit.Test


class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun newUserUsesApp() {
        composeTestRule.setContent {
            ProfileScreen()
        }

        composeTestRule.onNodeWithStringId(R.string.default_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_allergies).assertIsDisplayed()

   //     composeTestRule.onNodeWithContentDescription("image").assertIsDisplayed()
        //    composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field).assertIsDisplayed()
//        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).assertIsDisplayed()
//        composeTestRule.onNodeWithStringId(R.string.welcome_screen_name_field)
//            .performTextInput("James Bond")
//        composeTestRule.onNodeWithStringId(R.string.welcome_screen_button).performClick()
//        composeTestRule.onNodeWithText("${composeTestRule.activity.getString(R.string.profile_screen_placeholder)} James Bond")
//            .assertIsDisplayed()
    }

}