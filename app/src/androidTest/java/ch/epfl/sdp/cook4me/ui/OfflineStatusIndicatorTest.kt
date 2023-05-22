package ch.epfl.sdp.cook4me.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.OfflineStatusIndicator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OfflineStatusIndicatorTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun assertDefaultInformationIsDisplayed(){
        composeTestRule.setContent {
            OfflineStatusIndicator()
        }

        composeTestRule.onNodeWithStringId(R.string.inform_offline_status).assertIsDisplayed()
        composeTestRule.onNodeWithTag("OfflineIcon").assertIsDisplayed()
    }
}