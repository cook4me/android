package ch.epfl.sdp.cook4me.ui.tupperware.form

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomDividerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun defaultDividerIsDisplayed() {
        composeTestRule.setContent {
            CustomDivider()
        }
        composeTestRule.onNodeWithTag("CustomDivider").assertExists()
    }
}
