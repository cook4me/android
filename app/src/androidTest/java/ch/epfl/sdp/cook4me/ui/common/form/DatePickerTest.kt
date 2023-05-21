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
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class DatePickerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            DatePicker(initialDate = Calendar.getInstance(), onDateChange = {})
        }

        composeTestRule.onNodeWithStringId(R.string.selected_date_text).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.select_date_button).assertIsDisplayed()
    }
}
