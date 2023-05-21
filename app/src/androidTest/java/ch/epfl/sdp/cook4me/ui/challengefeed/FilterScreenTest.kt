
package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalComposeUiApi::class)
@RunWith(AndroidJUnit4::class)
class FilterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            // Define a sample FilterUI composable to test
            val selectedFilters = remember { mutableStateListOf("Option1") }
            val selectedSortOption = remember { mutableStateOf("Sort1") }

            FilterUI(
                onCancelClick = {},
                onDoneClick = {},
                onResetClick = {},
                onFilterOptionSelect = { option -> selectedFilters.add(option) },
                onSortSelect = { option -> selectedSortOption.value = option },
                sortOptions = listOf("Sort1", "Sort2"),
                filterCategories = mapOf("Category1" to listOf("Option1", "Option2")),
                selectedSortOption = selectedSortOption.value,
                selectedFilters = selectedFilters
            )
        }
    }

    @Test
    fun testFilterCategoryExpandedRetracted() {
        // Initially, the filter options should not be visible
        composeTestRule.onNodeWithText("Option1").assertDoesNotExist()
        composeTestRule.onNodeWithText("Option2").assertDoesNotExist()

        // Click on the category to expand it
        composeTestRule.onNodeWithText("Category1").performClick()

        // Now, the filter options should be visible
        composeTestRule.onNodeWithText("Option1").assertExists()
        composeTestRule.onNodeWithText("Option2").assertExists()

        // Click on the category again to retract it
        composeTestRule.onNodeWithText("Category1").performClick()

        // The filter options should not be visible anymore
        composeTestRule.onNodeWithText("Option1").assertDoesNotExist()
        composeTestRule.onNodeWithText("Option2").assertDoesNotExist()
    }

    @Test
    fun testFilterOptionSelected() {
        composeTestRule.onNodeWithText("Category1").performClick()

        // Select a filter option
        composeTestRule.onNodeWithText("Option2").performClick()

        // A tick icon should appear next to the selected filter option
        composeTestRule.onNodeWithContentDescription("Option2 selected").assertExists()
    }

    @Test
    fun testSortOptionSelected() {
        // Select a sort option
        composeTestRule.onNodeWithText("Sort2").performClick()

        // A tick icon should appear next to the selected sort option
        composeTestRule.onNodeWithContentDescription("Sort2 selected").assertExists()
    }
}
