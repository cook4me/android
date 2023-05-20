package ch.epfl.sdp.cook4me.ui.components

// @RunWith(AndroidJUnit4::class)
// class BulletPointTextFieldTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private fun inputTextIsCorrectlyDisplayed(
//        separator: Sequence<String> = GenericSeparators.BulletSeparator,
//        inputText: String,
//        expectedText: String,
//    ) {
//        composeTestRule.setContent {
//            BulletPointTextField(
//                separators = separator, contentDescription = "TextField", onValueChange = {}
//            )
//        }
//        val textField = composeTestRule.onNodeWithContentDescription("TextField")
//        textField.performTextInput(inputText)
//        textField.performClick()
//        textField.assertTextEquals(expectedText)
//    }

//    @Test
//    fun textIsDisplayedInCorrectFormat() {
//        inputTextIsCorrectlyDisplayed(
//            separator = GenericSeparators.BulletSeparator,
//            inputText = "Hey,\nHow\nAre\nYou",
//            expectedText = "• Hey,\n• How\n• Are\n• You"
//        )
//    }

//    @Test
//    fun textFieldIgnoresLeadingWhitespacesInBulletPoint() {
//        inputTextIsCorrectlyDisplayed(
//            separator = GenericSeparators.BulletSeparator,
//            inputText = "    Hey,\n   How\n Are\n   You",
//            expectedText = "• Hey,\n• How\n• Are\n• You",
//        )
//    }

//    @Test
//    fun textFieldDisplaysTextCorrectlyWithSeparatorsOfVaryingLength() {
//        val varyingLengthSeparator = sequenceOf("1. ", "22. ", "333. ", "4444. ")
//        inputTextIsCorrectlyDisplayed(
//            separator = varyingLengthSeparator,
//            inputText = "Hey,\nHow\nAre\nYou",
//            expectedText = "1. Hey,\n22. How\n333. Are\n4444. You"
//        )
//    }
// }
