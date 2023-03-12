package ch.epfl.sdp.cook4me

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.TimePickerComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class TimePickerComponentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed(){
        composeTestRule.setContent {
            TimePickerComponent(onTimeChanged = {})
        }

        composeTestRule.onNodeWithText("Selected Time: ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select time").assertIsDisplayed()
    }

    // TODO: fix this test
//    @Test
//    fun onTimeChangedIsCalledWhenTimeIsChanged(){
//        var time = Calendar.getInstance()
//        composeTestRule.setContent {
//            TimePickerComponent(onTimeChanged = {time = it})
//        }
//
//        composeTestRule.onNodeWithText("Select time").performClick()
//        composeTestRule.onNodeWithText("OK", useUnmergedTree = true, ignoreCase = true).performClick()
//        assert(time != Calendar.getInstance())
//    }
}