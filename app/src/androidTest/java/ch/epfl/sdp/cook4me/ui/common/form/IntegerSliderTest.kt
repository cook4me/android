package ch.epfl.sdp.cook4me.ui.common.form

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
class IntegerSliderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            MayParticipantsPicker(textRes = R.string.label, onValueChange = {})
        }

        composeTestRule.onNodeWithText("${getStringFromId(R.string.label)}").assertIsDisplayed()
    }

    private fun getStringFromId(id: Int) = composeTestRule.activity.getString(id)
}
