package ch.epfl.sdp.cook4me.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Post
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class PostDetailsTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testDefaultValuesAreDisplayed() {
        composeTestRule.setContent {
            PostDetails(
                painter = painterResource(id = R.drawable.tiramisu),
                data = Post(1, "Tiramisu", "This is tiramisu")
            )
        }

        composeTestRule.onNodeWithStringId(R.string.post_title).assertIsDisplayed()
        composeTestRule.onNodeWithText("Tiramisu").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is tiramisu").assertIsDisplayed()
        composeTestRule.onNodeWithTag("image").assertIsDisplayed()
    }
}
