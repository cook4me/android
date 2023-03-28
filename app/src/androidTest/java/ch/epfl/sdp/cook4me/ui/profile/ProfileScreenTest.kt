package ch.epfl.sdp.cook4me.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import ch.epfl.sdp.cook4me.R
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testChagedProfileDataAreDisplayed() {
        var profileCreationViewModel = ProfileCreationViewModel()

        val imageTag = composeTestRule.activity.getString(R.string.tag_defaultProfileImage)
        val bioText = "Hello chicas e chicos"
        val favDishText = "empanadas"
        val usernameText = "Emanuel"
        val allergiesText = "nada"
        val userImage = "".toUri()

        profileCreationViewModel.addBio(bioText)
        profileCreationViewModel.addUsername(favDishText)
        profileCreationViewModel.addAllergies(usernameText)
        profileCreationViewModel.addFavoriteDish(allergiesText)
        profileCreationViewModel.addUserImage(userImage)
        profileCreationViewModel.onSubmit()

        composeTestRule.setContent {
            ProfileScreen(profileCreationViewModel = profileCreationViewModel)
        }

        composeTestRule.onNodeWithText(bioText).assertIsDisplayed()
        composeTestRule.onNodeWithText(favDishText).assertIsDisplayed()
        composeTestRule.onNodeWithText(usernameText).assertIsDisplayed()
        composeTestRule.onNodeWithText(allergiesText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(imageTag).assertIsDisplayed()
    }
}
