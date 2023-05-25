package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CircleButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun defaultCircleButtonIsDisplayed() {
        val mockIcon = ImageVector.Builder(
            defaultHeight = 1.dp,
            defaultWidth = 1.dp,
            viewportWidth = 1f,
            viewportHeight = 1f
        ).build()
        composeTestRule.setContent {
            CircleButton(onClick = {}, icon = mockIcon, color = Color.Black)
        }
        composeTestRule.onNodeWithTag("CircleButton").assertExists()
    }

    @Test
    fun clickingOnIconCallsOnClick(){
        var clicked = false
        val mockIcon = ImageVector.Builder(
            defaultHeight = 1.dp,
            defaultWidth = 1.dp,
            viewportWidth = 1f,
            viewportHeight = 1f
        ).build()
        composeTestRule.setContent {
            CircleButton(onClick = {clicked = true}, icon = mockIcon, color = Color.Black)
        }
        composeTestRule.onNodeWithTag("CircleButton").performClick()
        assertThat(clicked, `is`(true))
    }
}