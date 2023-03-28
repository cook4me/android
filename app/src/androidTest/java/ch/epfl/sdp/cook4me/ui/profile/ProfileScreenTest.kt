package ch.epfl.sdp.cook4me.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testDefaultValuesAreDisplayed() {
        composeTestRule.setContent {
            ProfileScreen(viewModel = ProfileCreationViewModel())
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
