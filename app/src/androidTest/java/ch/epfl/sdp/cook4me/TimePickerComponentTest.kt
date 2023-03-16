package ch.epfl.sdp.cook4me


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.TimePickerComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimePickerComponentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed(){
        composeTestRule.setContent {
            TimePickerComponent(onTimeChanged = {})
        }

        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.selected_time_text).assertIsDisplayed()
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