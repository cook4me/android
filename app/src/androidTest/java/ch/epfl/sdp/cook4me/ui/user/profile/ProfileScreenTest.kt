package ch.epfl.sdp.cook4me.ui.user.profile

// class ProfileScreenTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var context: Context
//    private lateinit var repository: ProfileRepository
//    private lateinit var profileImageRepository: ProfileImageRepository
//    val profileImage = Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
//
//    private val user = Profile(
//        email = "donald.duck@epfl.ch",
//        name = "Donald",
//        allergies = "Hazelnut",
//        bio = "I am a duck",
//        favoriteDish = "Spaghetti",
//    )

//    @Before
//    fun setUp() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        /*
//        * IMPORTANT:
//        * (Below code is already functional, no need to change anything)
//        * Make sure you do this try-catch block,
//        * otherwise when doing CI, there will be an exception:
//        * kotlin.UninitializedPropertyAccessException: lateinit property firestore has not been initialized
//        * */
//        try {
//            Firebase.firestore.useEmulator("10.0.2.2", 8080)
//        } catch (e: IllegalStateException) {
//            // emulator already set
//            // do nothing
//        }
//        try {
//            Firebase.auth.useEmulator("10.0.2.2", 9099)
//        } catch (e: IllegalStateException) {
//            // emulator already set
//            // do nothing
//        }
//        repository = ProfileRepository()
//        auth = FirebaseAuth.getInstance()
//        profileImageRepository = ProfileImageRepository()
//        runBlocking {
//            auth.createUserWithEmailAndPassword(user.email, "123456").await()
//            auth.signInWithEmailAndPassword(user.email, "123456").await()
//            repository.add(user)
//            profileImageRepository.add(profileImage)
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            // delete the user from the database
//            repository.delete(user.email)
//            profileImageRepository.delete()
//            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun profileImageIsDisplayedTest() {
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.setContent {
//            ProfileScreen(
//                profileViewModel = profileViewModel
//            )
//        }
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        composeTestRule.onNodeWithTag("defaultProfileImage").assertExists()
//    }

//    @Test
//    fun profileLoadCorrectValuesTest() {
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.setContent {
//            ProfileScreen(
//                profileViewModel = profileViewModel
//            )
//        }
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        composeTestRule.onNodeWithText(user.name).assertExists()
//        composeTestRule.onNodeWithText(user.favoriteDish).assertExists()
//        composeTestRule.onNodeWithText(user.allergies).assertExists()
//        composeTestRule.onNodeWithText(user.bio).assertExists()
//    }

//    @Test
//    fun profileScreenStateTest() {
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        composeTestRule.setContent { ProfileScreen(profileViewModel = profileViewModel) }
//
//        profileViewModel.isLoading.value = true
//
//        // Wait for a moment to allow Compose to recompose
//        composeTestRule.waitForIdle()
//
//        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()
//
//        profileViewModel.isLoading.value = false
//
//        // Wait for a moment to allow Compose to recompose
//        composeTestRule.waitForIdle()
//
//        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertDoesNotExist()
//    }
// }
