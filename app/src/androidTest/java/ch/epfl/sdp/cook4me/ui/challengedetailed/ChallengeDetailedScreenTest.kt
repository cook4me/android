package ch.epfl.sdp.cook4me.ui.challengedetailed

// private const val CHALLENGE_PATH = "challenges"
// @RunWith(AndroidJUnit4::class)
// class ChallengeDetailedScreenTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//    private lateinit var context: Context
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var auth: FirebaseAuth
//    private lateinit var challengeId: String
//
//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        val (auth, firestore, challengeId) = setUpEvents(CHALLENGE_PATH)
//        this.auth = auth
//        this.firestore = firestore
//        this.challengeId = challengeId
//    }

//    @After
//    fun cleanUp() {
//        cleanUpEvents(auth, firestore, challengeId, CHALLENGE_PATH)
//    }

//    @Test
//    fun testChallengeDetailedScreen() {
//        composeTestRule.setContent {
//            ChallengeDetailedScreen(
//                challengeId = challengeId,
//                onVote = {},
//            )
//        }
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("Join")
//                .fetchSemanticsNodes().size == 1
//        }
//
//        composeTestRule.onNodeWithText("Mountain Climbing").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Climb the highest peak of the city!").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Time remaining: ").assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription("Participants").assertIsDisplayed()
//        composeTestRule.onNodeWithText("John").assertIsDisplayed()
//        composeTestRule.onNodeWithText("1").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Jane").assertIsDisplayed()
//        composeTestRule.onNodeWithText("2").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Creator: Admin").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Type: Spanish").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Join").assertIsDisplayed()
//    }

//    @Test
//    fun currentUserSuccessWhenJoiningEvent() {
//        composeTestRule.setContent {
//            ChallengeDetailedScreen(
//                challengeId = challengeId,
//                onVote = {},
//            )
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("Join")
//                .fetchSemanticsNodes().size == 1
//        }
//
//        composeTestRule.onNodeWithText("Join").performClick()
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("You have joined the challenge!")
//                .fetchSemanticsNodes().size == 1
//        }
//
//        // Assert user has been added and UI is updated
//        composeTestRule.onNodeWithText("harry.potter@epfl.ch").assertIsDisplayed()
//        composeTestRule.onNodeWithText("0").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Vote for other participants").assertIsDisplayed()
//    }
// }
