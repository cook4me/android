package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.IntegerSlider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegerSliderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            IntegerSlider(text = "text", min = 0, max = 10, onValueChange = {})
        }
        composeTestRule.onNodeWithText("text:0").assertIsDisplayed()
    }

    @Test
    fun integerSlider_updatesValue_whenDragged() {
        var value = 0
        composeTestRule.setContent {
            IntegerSlider(
                text = "Slider Value",
                min = 0,
                max = 10,
                onValueChange = { value = it }
            )
        }
        // Verify initial value
        composeTestRule.onNodeWithText("Slider Value:0").assertExists()
        // Drag the slider to the right
        composeTestRule.onNodeWithTag("slider").performTouchInput {
            swipeRight(
                startX = 0f,
                endX = 2000f,
                durationMillis = 2000
            )
        }
        // Verify the updated value
        print(value)
        composeTestRule.onNodeWithText("Slider Value:10").assertExists()
    }
}
