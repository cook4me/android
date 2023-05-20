package ch.epfl.sdp.cook4me.ui.navigation

// class Cook4MeNavigationFlowTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var store: FirebaseFirestore
//    private lateinit var context: Context
//    private lateinit var navController: NavHostController
//
//    private val currentRoute: String?
//        get() = navController.currentBackStackEntry?.destination?.route
//
//    private val permissionStatusProvider = TestPermissionStatusProvider(
//        initialPermissions = mapOf(
//            "TestPermission1" to Pair(true, true),
//            "TestPermission2" to Pair(true, true)
//        )
//    )
//    private fun getString(id: Int): String {
//        return composeTestRule.activity.getString(id)
//    }
//
//    private fun navigateToMainDestinationTest(destination: BottomNavScreen) {
//        composeTestRule.onNodeWithText(destination.title).performClick()
//        assertEquals(currentRoute, destination.route)
//    }
//
//    private fun navigateToXFromDropDownMenu(destination: BottomNavScreen) {
//        composeTestRule.onNodeWithText("More").performClick()
//        composeTestRule.waitUntilExists(hasText(destination.title))
//        composeTestRule.onNodeWithText(destination.title).performScrollTo().performClick()
//    }
//
//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        store = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setHost("10.0.2.2:8080") // connect to local firestore emulator
//            .setSslEnabled(false)
//            .setPersistenceEnabled(false)
//            .build()
//        store.firestoreSettings = settings
//        auth = FirebaseAuth.getInstance()
//        runBlocking {
//            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//        }
//
//        composeTestRule.setContent {
//            navController = TestNavHostController(LocalContext.current)
//            navController.navigatorProvider.addNavigator(ComposeNavigator())
//            Cook4MeApp(
//                navController = navController,
//                permissionStatusProvider = permissionStatusProvider
//            )
//        }
//    }

//    @Test
//    fun startScreenIsCorrectlyShown() {
//        assertEquals(currentRoute, authenticatedStartScreen)
//    }

//    @Test
//    fun navigatingToTupperwaresThroughBottomBar() {
//        navigateToMainDestinationTest(BottomNavScreen.Tupperwares)
//    }

//    @Test
//    fun navigatingToRecipesThroughBottomBar() {
//        navigateToMainDestinationTest(BottomNavScreen.Recipes)
//    }

//    @Test
//    fun navigatingToEventsThroughBottomBar() {
//        navigateToMainDestinationTest(BottomNavScreen.Events)
//    }

//    @Test
//    fun navigatingToCreateTupperwares() {
//        navController.navigateUp()
//        navigateToMainDestinationTest(BottomNavScreen.Tupperwares)
//        composeTestRule.onNodeWithText("Create a new Tupperware").performClick()
//        assertEquals(currentRoute, Screen.CreateTupperwareScreen.name)
//    }

//    @Test
//    fun navigatingToCreateRecipes() {
//        navController.navigateUp()
//        navigateToMainDestinationTest(BottomNavScreen.Recipes)
//        composeTestRule.onNodeWithText("Create a new Recipe").performClick()
//        assertEquals(currentRoute, Screen.CreateRecipeScreen.name)
//    }

//    @Test
//    fun navigatingToCreateEvents() {
//        navController.navigateUp()
//        navigateToMainDestinationTest(BottomNavScreen.Events)
//        composeTestRule.onNodeWithText("Create a new Event").performClick()
//        assertEquals(currentRoute, Screen.CreateEventScreen.name)
//    }

//    @Test
//    fun navigateToProfileScreen() {
//        navigateToXFromDropDownMenu(BottomNavScreen.Profile)
//        assertEquals(currentRoute, BottomNavScreen.Profile.route)
//    }

//    @Test
//    fun navigateToSignOutAndDisconnectTest() {
//        composeTestRule.onNodeWithText("More").performClick()
//        composeTestRule.waitUntilExists(hasText("Sign Out"))
//        composeTestRule.onNodeWithText("Sign Out").performScrollTo().performClick()
//        composeTestRule.onNodeWithTag(getString(R.string.login_screen_tag))
//        assertEquals(auth.currentUser, null)
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//            auth.currentUser?.delete()
//        }
//    }
// }
