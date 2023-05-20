package ch.epfl.sdp.cook4me.ui.challenge

// @RunWith(AndroidJUnit4::class)
// class RankingScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun rankingScreen_displaysParticipants() {
//        composeTestRule.setContent {
//            RankingScreen(
//                Challenge(
//                    name = "name",
//                    description = "description",
//                    dateTime = Calendar.getInstance(),
//                    participants = mapOf(
//                        "Alice" to 10,
//                        "Benjamin" to 22,
//                        "Charles the char" to 10,
//                        "Dobby the elf" to 80,
//                        "Esther the es" to 40
//                    ),
//                    creator = "darth.vader@epfl.ch",
//                    type = "French"
//                ),
//                {}
//            )
//        }
//
//        composeTestRule.onNodeWithText("Ranking").assertExists()
//
//        composeTestRule.onNodeWithText("Place").assertExists()
//        composeTestRule.onNodeWithText("Score").assertExists()
//        composeTestRule.onNodeWithText("Participant").assertExists()
//
//        composeTestRule.onNodeWithText("80").assertExists()
//        composeTestRule.onNodeWithText("22").assertExists()
//
//        composeTestRule.onNodeWithText("Dobby the elf ").assertExists()
//        composeTestRule.onNodeWithContentDescription("Winner").assertExists()
//        composeTestRule.onNodeWithText("Esther the es ").assertExists()
//        composeTestRule.onNodeWithText("Benjamin ").assertExists()
//        composeTestRule.onNodeWithText("1.").assertExists()
//        composeTestRule.onNodeWithText("2.").assertExists()
//        composeTestRule.onNodeWithText("3.").assertExists()
//    }

//    @Test
//    fun rankingScreen_displaysBackButton() {
//        var onClick = false
//
//        val challenge = Challenge(
//            participants = mapOf()
//        )
//
//        composeTestRule.setContent {
//            RankingScreen(challenge) { onClick = true }
//        }
//
//        composeTestRule.onNodeWithText("Back").assertExists()
//        composeTestRule.onNodeWithText("Back").performClick()
//        assert(onClick)
//    }
// }
