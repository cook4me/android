package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.DatePickerComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatePickerComponentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed(){
        composeTestRule.setContent {
            DatePickerComponent(initialDate = Calendar.getInstance(), onDateChange = {})
        }

        composeTestRule.onNodeWithText("Selected Date: ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select Date").assertIsDisplayed()
    }
}