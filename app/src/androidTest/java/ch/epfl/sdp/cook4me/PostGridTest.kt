package ch.epfl.sdp.cook4me.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class PostGridTest {

    @ExperimentalFoundationApi
    class PostGridTest {

        @get:Rule
        val composeTestRule = createComposeRule()

        @Test
        fun postGrid_showsCorrectImages() {
            // Launch the composable
            composeTestRule.setContent {
                PostGrid()
            }

            // Verify that the expected images are displayed
            composeTestRule.onNodeWithContentDescription("Grid Image")
                .assert(hasAnySibling(hasText("Carbonara")))
        }
    }
}
