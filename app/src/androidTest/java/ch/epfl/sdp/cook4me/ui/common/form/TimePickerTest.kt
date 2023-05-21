package ch.epfl.sdp.cook4me.ui.common.form

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
class TimePickerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            TimePicker(onTimeChanged = {})
        }

        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.selected_time_text).assertIsDisplayed()
    }
}
