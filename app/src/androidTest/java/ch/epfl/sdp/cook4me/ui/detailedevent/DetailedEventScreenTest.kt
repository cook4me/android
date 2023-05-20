package ch.epfl.sdp.cook4me.ui.detailedevent

// private const val EVENT_PATH = "events"
// @RunWith(AndroidJUnit4::class)
// class DetailedEventScreenTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//    private lateinit var context: Context
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var auth: FirebaseAuth
//    private lateinit var eventId: String

//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        val (auth, firestore, eventId) = setUpEvents(EVENT_PATH)
//        this.auth = auth
//        this.firestore = firestore
//        this.eventId = eventId
//    }

//    @After
//    fun cleanUp() {
//        cleanUpEvents(auth, firestore, eventId, EVENT_PATH)
//    }

//    @Test
//    fun testCorrectDetailedEventInfoIsDisplayed() {
//        composeTestRule.setContent {
//            DetailedEventScreen(eventId)
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("test event name")
//                .fetchSemanticsNodes().size == 1
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.event_name).assertIsDisplayed()
//        composeTestRule.onNodeWithText("test event name").assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_description).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_description).assertIsDisplayed()
//        composeTestRule.onNodeWithText("test description").performScrollTo()
//        composeTestRule.onNodeWithText("test description").assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_location).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_location).assertIsDisplayed()
//        composeTestRule.onNodeWithText("mondstadt").performScrollTo()
//        composeTestRule.onNodeWithText("mondstadt").assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_location).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_creator).assertIsDisplayed()
//        composeTestRule.onNodeWithText("peter griffin").performScrollTo()
//        composeTestRule.onNodeWithText("peter griffin").assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_who_can_see_event).assertIsDisplayed()
//        composeTestRule.onNodeWithText("Everyone").performScrollTo()
//        composeTestRule.onNodeWithText("Everyone").assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_max_participants).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_max_participants).assertIsDisplayed()
//        composeTestRule.onNodeWithText("4").performScrollTo()
//        composeTestRule.onNodeWithText("4").assertIsDisplayed()
//
//        val testEventParticipants = testEvent.participants.joinToString(separator = ", ")
//        composeTestRule.onNodeWithStringId(R.string.event_participants).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_participants).assertIsDisplayed()
//        composeTestRule.onNodeWithText(testEventParticipants).performScrollTo()
//        composeTestRule.onNodeWithText(testEventParticipants).assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.event_time).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.event_time).assertIsDisplayed()
//        composeTestRule.onNodeWithText(testEvent.eventDate).performScrollTo()
//        composeTestRule.onNodeWithText(testEvent.eventDate).assertIsDisplayed()
//
//        composeTestRule.onNodeWithStringId(R.string.detailed_event_create_chat).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.detailed_event_create_chat).assertIsDisplayed()
//    }

//    @Test
//    fun testDetailedEventScreenShowsLoadingScreen() {
//        composeTestRule.setContent {
//            DetailedEventScreen("somefakeid")
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithTag(context.getString(R.string.Loading_Screen_Tag))
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithTag(context.getString(R.string.Loading_Screen_Tag)).assertIsDisplayed()
//    }
// }
