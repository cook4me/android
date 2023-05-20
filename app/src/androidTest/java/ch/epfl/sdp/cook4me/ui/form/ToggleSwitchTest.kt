package ch.epfl.sdp.cook4me.ui.form

// @RunWith(AndroidJUnit4::class)
// class ToggleSwitchTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    @Test
//    fun defaultInformationIsDisplayed() {
//        composeTestRule.setContent {
//            ToggleSwitch(
//                question = R.string.question,
//                answerChecked = R.string.option1,
//                answerUnchecked = R.string.option2,
//                onToggle = {}
//            )
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.question).assertIsDisplayed()
//        composeTestRule.onNodeWithStringId(R.string.option1).assertIsDisplayed()
//    }

//    @Test
//    fun onToggleIsCalledWhenSwitchIsPressed() {
//        var toggle = true
//        composeTestRule.setContent {
//            ToggleSwitch(
//                question = R.string.question,
//                answerChecked = R.string.option1,
//                answerUnchecked = R.string.option2,
//                onToggle = { toggle = it }
//            )
//        }
//
//        composeTestRule.onNodeWithTag("switch").performClick()
//        assert(!toggle)
//        composeTestRule.onNodeWithTag("switch").performClick()
//        assert(toggle)
//    }
// }
