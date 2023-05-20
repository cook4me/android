package ch.epfl.sdp.cook4me.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// @Composable
// private fun TestableDropDownMenu(options: List<String>) {
//    var selected by remember {
//        mutableStateOf(options[0])
//    }
//
//    CustomDropDownMenu(
//        options = options,
//        value = selected,
//        onValueChange = { selected = it },
//        contentDescription = ""
//    )
// }
//
// @RunWith(AndroidJUnit4::class)
// class DropDownMenuTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val options = listOf("element1", "element2", "element3", "element4")

//    @Test
//    fun defaultValueIsDisplayed() {
//        composeTestRule.setContent {
//            TestableDropDownMenu(options = options)
//        }
//
//        composeTestRule.onNodeWithText("element1").assertIsDisplayed()
//    }

//    @Test
//    fun dropDownMenuExpandsWhenClickedOn() {
//        composeTestRule.setContent {
//            TestableDropDownMenu(options = options)
//        }
//
//        composeTestRule.onNodeWithText("element1").performClick()
//        options.forEach {
//            composeTestRule.onAllNodesWithText(it)[0].assertIsDisplayed()
//        }
//    }

//    @Test
//    fun dropDownMenuItemIsClickable() {
//        composeTestRule.setContent {
//            TestableDropDownMenu(options = options)
//        }
//
//        composeTestRule.onNodeWithText("element1").performClick()
//        composeTestRule.onNodeWithText("element3").performClick()
//        composeTestRule.onNodeWithText("element1").assertDoesNotExist()
//        composeTestRule.onNodeWithText("element3").assertIsDisplayed()
//    }

//    @Test
//    fun longMenuIsScrollable() {
//        val longOptionsList = generateSequence(1) { it + 1 }
//            .map { it.toString() }
//            .take(50)
//            .toList()
//
//        composeTestRule.setContent {
//            TestableDropDownMenu(options = longOptionsList)
//        }
//
//        composeTestRule.onNodeWithText("1").performClick()
//        composeTestRule.onNodeWithText("50").performScrollTo()
//        composeTestRule.onNodeWithText("50").assertIsDisplayed()
//    }
// }
