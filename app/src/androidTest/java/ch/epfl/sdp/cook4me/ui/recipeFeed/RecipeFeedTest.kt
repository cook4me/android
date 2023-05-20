package ch.epfl.sdp.cook4me.ui.recipeFeed

// @RunWith(AndroidJUnit4::class)
// class RecipeFeedTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val mockRecipeFeedService = mockk<RecipeFeedService>(relaxed = true)
//
//    private val mockList = listOf(
//        RecipeNote(recipeId = "id1", recipe = Recipe(name = "Test recipe 1", cookingTime = "10 min"), note = 1),
//        RecipeNote(recipeId = "id2", recipe = Recipe(name = "Test recipe 2", cookingTime = "20 min"), note = 2)
//    )

//    @Test
//    fun defaultBottomBarIsCorrectlyDisplayed() {
//        composeTestRule.setContent {
//            BottomBar()
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.get_top_recipes).assertIsDisplayed()
//        composeTestRule.onNodeWithStringId(R.string.get_recent_recipes).assertIsDisplayed()
//    }

//    @Test
//    fun defaultRecipeFeedIsCorrectlyDisplayed() {
//        coEvery { mockRecipeFeedService.getRecipesWithNotes() } returns mockList
//        composeTestRule.setContent {
//            RecipeFeed(mockRecipeFeedService)
//        }
//
//        for (recipe in mockList) {
//            composeTestRule.onNodeWithText(recipe.recipe.name).assertIsDisplayed()
//            composeTestRule.onNodeWithText(recipe.recipe.cookingTime).assertIsDisplayed()
//            composeTestRule.onNodeWithText(recipe.note.toString()).assertIsDisplayed()
//        }
//    }
// }
