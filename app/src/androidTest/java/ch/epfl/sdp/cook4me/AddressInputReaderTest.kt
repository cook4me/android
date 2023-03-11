package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.AddressInputReader
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressInputReaderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed(){
        composeTestRule.setContent {
            AddressInputReader(question = "question", onAddressChanged = {})
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("Street address").assertIsDisplayed()
        composeTestRule.onNodeWithText("City").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zip code").assertIsDisplayed()
    }

    @Test
    fun onAddressChangedIsCalledWhenTextIsChanged(){
        var address = ""
        composeTestRule.setContent {
            AddressInputReader(question = "question", onAddressChanged = {address = it})
        }

        composeTestRule.onNodeWithText("Street address").performTextInput("street")
        composeTestRule.onNodeWithText("City").performTextInput("city")
        composeTestRule.onNodeWithText("Zip code").performTextInput("zip")

        assert(address == "street, zip city")
    }
}