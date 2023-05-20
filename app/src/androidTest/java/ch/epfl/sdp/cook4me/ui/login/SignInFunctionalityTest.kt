package ch.epfl.sdp.cook4me.ui.login

// @ExperimentalCoroutinesApi
// @RunWith(AndroidJUnit4::class)
// class SignInFunctionalityTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var context: Context
//    private val testTagEmailField = "EmailField"
//    private val testTagPasswordField = "PasswordField"

//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        auth = FirebaseAuth.getInstance()
//        runBlocking {
//            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun validUserSignInSuccessfully() = runTest {
//        auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//        assertThat(auth.currentUser?.email, `is`("harry.potter@epfl.ch"))
//    }

//    @Test
//    fun invalidUserSignInTriggersException() = runTest {
//        assertThrowsAsync {
//            auth.signInWithEmailAndPassword("mrinvalid@epfl.ch", "hahaha").await()
//        }
//    }

//    @Test
//    fun logInScreenValidUserWithCorrectPasswordSignInSuccessfully() = runTest {
//        var wasCalled = false
//        composeTestRule.setContent {
//            LoginScreen { wasCalled = true }
//        }
//        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("harry.potter@epfl.ch")
//        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("123456")
//        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            wasCalled
//        }
//        assertTrue(wasCalled)
//    }

//    @Test
//    fun logInScreenNonExistUserSignInShowsNonExistUserMessage() = runTest {
//        composeTestRule.setContent {
//            LoginScreen {}
//        }
//        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("mr.nonexist@epfl.ch")
//        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("123456")
//        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText(context.getString(R.string.sign_in_screen_non_exist_user))
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_non_exist_user)).assertIsDisplayed()
//    }

//    @Test
//    fun logInScreenValidUserWithWrongPasswordShowsWrongPasswordMessage() = runTest {
//        composeTestRule.setContent {
//            LoginScreen {}
//        }
//        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("harry.potter@epfl.ch")
//        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("1234567")
//        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithText(context.getString(R.string.sign_in_screen_wrong_password))
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_wrong_password)).assertIsDisplayed()
//    }
// }
