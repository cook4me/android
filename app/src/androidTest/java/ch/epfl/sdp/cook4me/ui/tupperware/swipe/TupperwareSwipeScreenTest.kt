package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupperwareSwipeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun placeholderScreenIsShown() {
        composeTestRule.setContent {
            TupperwareSwipeScreen()
        }
        composeTestRule.onNodeWithText(getString(R.string.swipe_alldone_placeholder)).assertIsDisplayed()
    }

    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }
}
