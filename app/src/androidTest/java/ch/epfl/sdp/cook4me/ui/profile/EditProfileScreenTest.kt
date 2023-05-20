package ch.epfl.sdp.cook4me.ui.profile

// class EditProfileScreenTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var profileImageRepository: ProfileImageRepository
//    private lateinit var store: FirebaseFirestore
//    private lateinit var storage: FirebaseStorage
//    private lateinit var auth: FirebaseAuth
//    private lateinit var repository: ProfileRepository
//    private var userImage: Uri =
//        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_launcher_foreground")
//    private val user = Profile(
//        email = "donald.duck@epfl.ch",
//        name = "Donald",
//        allergies = "Hazelnut",
//        bio = "I am a duck",
//        favoriteDish = "Spaghetti",
//    )

//    @Before
//    fun setUp() {
//        // Connect to local firestore emulator
//        store = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setHost("10.0.2.2:8080") // connect to local firestore emulator
//            .setSslEnabled(false)
//            .setPersistenceEnabled(false)
//            .build()
//        store.firestoreSettings = settings
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        storage = FirebaseStorage.getInstance()
//        storage.useEmulator("10.0.2.2", 9199)
//        repository = ProfileRepository(store)
//        auth = FirebaseAuth.getInstance()
//        profileImageRepository = ProfileImageRepository(store, storage, auth)
//        runBlocking {
//            auth.createUserWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
//            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
//            repository.add(user)
//            userImage = profileImageRepository.add(userImage)
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            repository.delete(user.email)
//            profileImageRepository.delete()
//            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun editScreenTestDisplayValues() {
//        val profileViewModel = ProfileViewModel()
//
//        // Set up the test and wait for the screen to be loaded
//        val usernameTag = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)
//        val favFoodTag = composeTestRule.activity.getString(R.string.tag_favoriteDish)
//        val allergiesTag = composeTestRule.activity.getString(R.string.tag_allergies)
//        val bioTag = composeTestRule.activity.getString(R.string.tag_bio)
//
//        // Set input
//        // This test does not work because of some issue not finding the text fields
//        // after the clearence of the text fields this happends on connected test
//        // but not when the test is run on its own
//        val usernameInput = "ronald"
//        val favoriteDishInput = "Butterbeer"
//        val allergiesInput = "Snails"
//        val bioInput = "I'm just the friend of harry"
//
//        // Set up the test
//        composeTestRule.setContent {
//            EditProfileScreen(
//                viewModel = profileViewModel
//            )
//        }
//
//        // Wait for the screen to be loaded
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        // Verify that the image is displayed
//        // composeTestRule.onNodeWithTag("tag_defaultProfileImage").assertExists()
//        composeTestRule.waitForIdle()
//
//        // Clear fields
//        composeTestRule.onNodeWithTag(usernameTag).performTextClearance()
//        composeTestRule.onNodeWithTag(favFoodTag).performTextClearance()
//        composeTestRule.onNodeWithTag(bioTag).performTextClearance()
//        composeTestRule.onNodeWithTag(allergiesTag).performTextClearance()
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule
//                .onAllNodesWithTag(usernameTag)
//                .fetchSemanticsNodes().size == 1
//        }
//
//        // Set input
//        composeTestRule.onNodeWithTag(usernameTag).performTextInput(usernameInput)
//        composeTestRule.onNodeWithTag(favFoodTag).performTextInput(favoriteDishInput)
//        composeTestRule.onNodeWithTag(allergiesTag).performTextInput(allergiesInput)
//        composeTestRule.onNodeWithTag(bioTag).performTextInput(bioInput)
//
//        // Wait ot be completed
//        composeTestRule.waitForIdle()
//
//        // Verify that the text fields display the correct values
//        composeTestRule.onNodeWithText(usernameInput).assertExists()
//        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
//        composeTestRule.onNodeWithText(allergiesInput).assertExists()
//        composeTestRule.onNodeWithText(bioInput).assertExists()
//
//        // Click on the save button
//        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
//    }

//    @Test
//    fun editProfileScreenStateTest() {
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.setContent {
//            EditProfileScreen(viewModel = profileViewModel)
//        }
//
//        profileViewModel.isLoading.value = true
//
//        // Wait for a moment to allow Compose to recompose
//        composeTestRule.waitForIdle()
//
//        // Check that the progress bar is displayed
//        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()
//
//        // Wait to be completed
//        profileViewModel.isLoading.value = false
//
//        // Wait for a moment to allow Compose to recompose
//        composeTestRule.waitForIdle()
//        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertDoesNotExist()
//    }

//    @Test
//    fun editProfileScreenCancelButtonTest() {
//        var isCancelledClicked = false
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.setContent {
//            EditProfileScreen(
//                onCancelListener = { isCancelledClicked = true },
//                viewModel = profileViewModel
//            )
//        }
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        // Check that the cancel button is not clicked
//        assert(!isCancelledClicked)
//
//        // Click on the cancel button
//        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
//
//        // Check that the cancel button is clicked
//        assert(isCancelledClicked)
//    }

//    @Test
//    fun editProfileScreenSaveButtonIsClicked() {
//        var isSaveClicked = false
//        val profileViewModel = ProfileViewModel()
//
//        composeTestRule.setContent {
//            EditProfileScreen(
//                onSuccessListener = { isSaveClicked = true },
//                viewModel = profileViewModel
//            )
//        }
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        // Check that the cancel button is not clicked
//        assert(!isSaveClicked)
//
//        // Click on the cancel button
//        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
//
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            !profileViewModel.isLoading.value
//        }
//
//        // Check that the cancel button is clicked
//        assert(isSaveClicked)
//    }
// }
