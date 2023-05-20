package ch.epfl.sdp.cook4me.ui.tupperwareform

//
// @RunWith(AndroidJUnit4::class)
// class TupperwareCreationScenarioTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val testUri: Uri = Uri.parse(
//        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.placeholder_tupperware
//    )
//
//    private val textFieldWithErrorMatcher =
//        SemanticsMatcher.expectValue(
//            SemanticsProperties.StateDescription, "Error"
//        )

//    @Test
//    fun submittingValidTupFormShouldOutputCorrectTupperwareObject() {
//        val mockTupperwareRepository = mockk<TupperwareRepository>(relaxed = true)
//        val expectedTitle = "Pizza"
//        val expectedDescription = "Yeah the photo is not lying it's not good..."
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateTupperwareScreen({}, {}, mockTupperwareRepository)
//            }
//        }
//        composeTestRule.onNodeWithTag("AddImage").performClick()
//        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
//        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
//
//        composeTestRule.onNodeWithTag("title").performTextInput(expectedTitle)
//        composeTestRule.onNodeWithTag("description")
//            .performTextInput(expectedDescription)
//        composeTestRule.onNodeWithText("Done").performClick()
//        verify {
//            runBlocking {
//                mockTupperwareRepository.add(
//                    expectedTitle,
//                    expectedDescription,
//                    testUri
//                )
//            }
//        }
//        confirmVerified(mockTupperwareRepository)
//    }

//    @Test
//    fun descriptionFieldIsDisplayed() {
//        composeTestRule.setContent {
//            CreateTupperwareScreen({}, {})
//        }
//        composeTestRule.onNodeWithText(text = "Description").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("description").assertIsDisplayed()
//    }

//    @Test
//    fun titleFieldIsDisplayed() {
//        composeTestRule.setContent {
//            CreateTupperwareScreen({}, {})
//        }
//        composeTestRule.onNodeWithText(text = "Tupperware Name").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("title").assertIsDisplayed()
//    }

//    @Test
//    fun headerIsDisplayed() {
//
//        composeTestRule.setContent {
//            CreateTupperwareScreen({}, {})
//        }
//        composeTestRule.onNodeWithText(text = "Header").assertIsDisplayed()
//    }

//    @Test
//    fun buttonRowIsDisplayed() {
//
//        composeTestRule.setContent {
//            CreateTupperwareScreen({}, {})
//        }
//        composeTestRule.onNodeWithText(text = "Cancel").assertIsDisplayed()
//        composeTestRule.onNodeWithText(text = "Done").assertIsDisplayed()
//    }

// no title or no image or no description
//    @Test
//    fun tupperwareFormWithNoTitleShouldNotBeSubmittable() {
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateTupperwareScreen({}, {})
//            }
//        }
//        composeTestRule.onNodeWithTag("AddImage").performClick()
//        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
//        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("description")
//            .performTextInput("Yeah the photo is not lying it's not good...")
//        composeTestRule.onNodeWithText("Done").performClick()
//        composeTestRule.onAllNodes(textFieldWithErrorMatcher)[0].assertExists()
//    }

// TODO: uncomment when image state handling is refactored similar to input field state handling
//    @Test
//    fun tupperwareFormWithNoImageShouldNotBeSubmittable() {
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateTupperwareScreen()
//            }
//        }
//
//        composeTestRule.onNodeWithTag("title").performTextInput("Pizza")
//        composeTestRule.onNodeWithTag("description")
//            .performTextInput("Yeah the photo is not lying it's not good...")
//        composeTestRule.onNodeWithText("Done").performClick()
//        composeTestRule.onAllNodes(textFieldWithErrorMatcher)[0].assertExists()
//    }

//    @Test
//    fun tupperwareFormWithNoDescriptionShouldNotBeSubmittable() {
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateTupperwareScreen({}, {})
//            }
//        }
//        composeTestRule.onNodeWithTag("AddImage").performClick()
//        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
//        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
//
//        composeTestRule.onNodeWithTag("title").performTextInput("Pizza")
//        composeTestRule.onNodeWithText("Done").performClick()
//        composeTestRule.onAllNodes(textFieldWithErrorMatcher)[0].assertExists()
//    }

//    @Test
//    fun addingImageFromGalleryShouldDisplayImage() {
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateTupperwareScreen({}, {})
//            }
//        }
//        composeTestRule.onNodeWithTag("AddImage").performClick()
//        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
//        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
//    }

//    private val registryOwner = object : ActivityResultRegistryOwner {
//        override val activityResultRegistry: ActivityResultRegistry =
//            object : ActivityResultRegistry() {
//                override fun <I : Any?, O : Any?> onLaunch(
//                    requestCode: Int,
//                    contract: ActivityResultContract<I, O>,
//                    input: I,
//                    options: ActivityOptionsCompat?
//                ) {
//                    // don't launch an activity, just respond with the test Uri
//                    val intent = Intent().setData(testUri)
//                    this.dispatchResult(requestCode, Activity.RESULT_OK, intent)
//                }
//            }
//    }
// }
