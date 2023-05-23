package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupperwareSwipeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenOfflineShouldDisplayDefaultMessage() {
        composeTestRule.setContent {
            TupperwareSwipeScreen(isOnline = false)
        }
        composeTestRule.onNodeWithStringId(R.string.to_swipe_go_online).assertIsDisplayed()
    }

//    @Test
//    fun placeholderScreenIsShown() {
//        composeTestRule.setContent {
//            TupperwareSwipeScreen()
//        }
//        composeTestRule.onNodeWithTag(getString(R.string.tupperware_swipe_screen_tag)).assertIsDisplayed()
//    }

//    private fun getString(id: Int): String {
//        return composeTestRule.activity.getString(id)
//    }
}
