package ch.epfl.sdp.cook4me.ui.challengeform

// class CreateChallengeScreenTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val mockChallengeService = mockk<ChallengeFormService>(relaxed = true)
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    @Test
//    fun assertAllQuestionsAreDisplayed() {
//        composeTestRule.setContent {
//            CreateChallengeScreen(mockChallengeService)
//        }
//        composeTestRule.onNodeWithStringId(R.string.ask_challenge_name).assertExists()
//        composeTestRule.onNodeWithStringId(R.string.ask_challenge_description).assertExists()
//        composeTestRule.onNodeWithStringId(R.string.select_date_button).assertExists()
//        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertExists()
//        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).assertExists()
//    }

//    @Test
//    fun submitIncorrectFormsShowsError() {
//        composeTestRule.setContent {
//            CreateChallengeScreen(mockChallengeService)
//        }
//
//        coEvery { mockChallengeService.submitForm(match { !it.isValidChallenge }) } returns "error"
//        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performClick()
//        composeTestRule.waitUntilExists(hasText("error"))
//        coVerify { mockChallengeService.submitForm(match { !it.isValidChallenge }) }
//    }

//    @Test
//    fun cancelButtonClickCallsOnCancel() {
//        var onCancelCalled = false
//        composeTestRule.setContent {
//            CreateChallengeScreen(
//                onCancelClick = {
//                    onCancelCalled = true
//                }
//            )
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
//        MatcherAssert.assertThat(onCancelCalled, Matchers.`is`(true))
//    }

//    private fun ComposeContentTestRule.waitUntilExists(
//        matcher: SemanticsMatcher,
//        timeoutMillis: Long = 5000
//    ) {
//        this.waitUntil(timeoutMillis) {
//            this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
//        }
//    }
// }
