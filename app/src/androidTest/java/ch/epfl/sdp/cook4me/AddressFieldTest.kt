package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.eventCreationForm.AddressField
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressFieldTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            AddressField(question = "question", onAddressChanged = {})
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.street_address_label).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.city_label).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.zip_code_label).assertIsDisplayed()
    }

    @Test
    fun onAddressChangedIsCalledWhenTextIsChanged() {
        var address = ""
        composeTestRule.setContent {
            AddressField(question = "question", onAddressChanged = { address = it })
        }

        composeTestRule.onNodeWithStringId(R.string.street_address_label).performTextInput("street")
        composeTestRule.onNodeWithStringId(R.string.city_label).performTextInput("city")
        composeTestRule.onNodeWithStringId(R.string.zip_code_label).performTextInput("zip")

        assert(address == "street, zip city")
    }
}
