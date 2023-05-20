package ch.epfl.sdp.cook4me.ui.form

// @RunWith(AndroidJUnit4::class)
// class InputFieldTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    @Test
//    fun questionLabelAndExampleTextIsDisplayed() {
//        composeTestRule.setContent {
//            InputField(
//                question = R.string.question, label = R.string.label,
//                value = "",
//                onValueChange = {}
//            )
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.question).assertIsDisplayed()
//        composeTestRule.onNodeWithStringId(R.string.label).assertIsDisplayed()
//    }

//    @Test
//    fun onTextChangedIsCalledWhenTextIsChanged() {
//        var text = ""
//        composeTestRule.setContent {
//            InputField(
//                question = R.string.question, label = R.string.label,
//                onValueChange = { text = it },
//                value = ""
//            )
//        }
//
//        composeTestRule.onNodeWithText("").performTextInput("new text")
//        assertEquals("new text", text)
//    }
// }
