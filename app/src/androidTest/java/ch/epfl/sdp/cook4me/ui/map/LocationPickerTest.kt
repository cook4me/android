package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class LocationPickerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mapIsDisplayed() {
        composeTestRule.setContent {
            LocationPicker { latLng ->
                println("Location picked: $latLng")
            }
        }

        composeTestRule.onRoot().printToLog("DEBUG")
        composeTestRule.onNodeWithContentDescription("Google Map")
            .assertIsDisplayed()
    }

}