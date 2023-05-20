package ch.epfl.sdp.cook4me.ui.recipeFeed

// @RunWith(AndroidJUnit4::class)
// class VoteIconTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

//    @Test
//    fun defaultVoteIconIsDisplayed() {
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0)
//        }
//
//        composeTestRule.onNodeWithContentDescription("Upvote").assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription("Downvote").assertIsDisplayed()
//        composeTestRule.onNodeWithText("0").assertIsDisplayed()
//    }

//    @Test
//    fun pressUpvoteIncreasesCounter() {
//        var counter = 0
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, onChange = { counter += it })
//        }
//
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithText("1").assertIsDisplayed()
//
//        assertThat(counter, `is`(1))
//    }

//    @Test
//    fun pressDownvoteDecreasesCounter() {
//        var counter = 0
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, onChange = { counter += it })
//        }
//
//        composeTestRule.onNodeWithContentDescription("Downvote").performClick()
//        composeTestRule.onNodeWithText("-1").assertIsDisplayed()
//
//        assertThat(counter, `is`(-1))
//    }

//    @Test
//    fun pressTwiceUpvoteGoesBackToDefault() {
//        var counter = 0
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, onChange = { counter += it })
//        }
//
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithText("0").assertIsDisplayed()
//
//        assertThat(counter, `is`(0))
//    }

//    @Test
//    fun pressUpvoteOnDownvotedButtonRemovesDownvote() {
//        var counter = 0
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, onChange = { counter += it })
//        }
//
//        composeTestRule.onNodeWithContentDescription("Downvote").performClick()
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithText("1").assertIsDisplayed()
//
//        assertThat(counter, `is`(1))
//    }

//    @Test
//    fun positiveUserVoteDisplayUpvotedButton() {
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, userVote = 1)
//        }
//
//        // this test verifies that the upvote button was pressed by verifying the behavior when the button is pressed
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithText("-1").assertIsDisplayed()
//    }

//    @Test
//    fun canClickAtFalseBlocksUserFromClicking() {
//        var counter = 0
//        composeTestRule.setContent {
//            VoteIcon(counterValue = 0, onChange = { counter += it }, canClick = false)
//        }
//
//        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
//        composeTestRule.onNodeWithText("0").assertIsDisplayed()
//
//        assertThat(counter, `is`(0))
//    }
// }
