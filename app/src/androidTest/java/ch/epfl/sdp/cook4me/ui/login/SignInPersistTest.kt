package ch.epfl.sdp.cook4me.ui.login

// @ExperimentalCoroutinesApi
// @RunWith(AndroidJUnit4::class)
// class SignInPersistTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var context: Context

//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        auth = FirebaseAuth.getInstance()
//        runBlocking {
//            auth.createUserWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            auth.signInWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun whenUserSignedInAppNavigatesToStartScreen() = runTest {
//        auth.signInWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
//        composeTestRule.setContent {
//            Cook4MeApp()
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText("Top recipes")
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithText("Top recipes").assertIsDisplayed()
//    }

//    @Test
//    fun whenNoUserSignedInAppNavigatesToLoginScreen() = runTest {
//        auth.signOut()
//        composeTestRule.setContent {
//            Cook4MeApp()
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithTag(context.getString(R.string.login_screen_tag))
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithTag(context.getString(R.string.login_screen_tag)).assertIsDisplayed()
//    }
// }
