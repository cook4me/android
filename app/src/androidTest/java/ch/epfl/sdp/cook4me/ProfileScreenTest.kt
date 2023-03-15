package ch.epfl.sdp.cook4me

import ProfileScreen
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testDefaultValuesAreDisplayed() {
        composeTestRule.setContent {
            ProfileScreen()
        }
        val imageTag = composeTestRule.activity.getString(R.string.tag_defaultProfileImage)

        composeTestRule.onNodeWithStringId(R.string.default_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithTag(imageTag).assertIsDisplayed()
    }
}
