package ch.epfl.sdp.cook4me.ui.recipe

// @RunWith(AndroidJUnit4::class)
// class RecipeScreenTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val dummyRecipe = Recipe(
//        name = "Pizza", ingredients = listOf("Tomato", "Cheese", "Dough"),
//        recipeSteps = listOf("Put the tomato on the dough", "Put the cheese on the tomato"),
//        difficulty = "Easy", cookingTime = "30min", servings = 4
//    )

//    @Test
//    fun titleCorrectlyDisplayed() {
//        composeTestRule.setContent {
//            RecipeScreen(dummyRecipe)
//        }
//        composeTestRule.onNodeWithText(dummyRecipe.name).assertIsDisplayed()
//    }

//    @Test
//    fun basicRecipeInfoDisplayedWithSeparator() {
//        composeTestRule.setContent {
//            RecipeScreen(dummyRecipe)
//        }
//        val recipeBasicInfo = "Servings: ${dummyRecipe.servings} | Cooking time: ${dummyRecipe.cookingTime} | Difficulty: ${dummyRecipe.difficulty}"
//        composeTestRule.onNodeWithText(recipeBasicInfo).assertIsDisplayed()
//    }

//    @Test
//    fun ingredientsAndStepsDisplayedAsDashedList() {
//        composeTestRule.setContent {
//            RecipeScreen(dummyRecipe)
//        }
//        val ingredients = "Ingredients: \n ${dummyRecipe.ingredients.map{s -> "\t - $s"}.joinToString("\n")}"
//        val steps = "Steps: \n ${dummyRecipe.recipeSteps.map{s -> "\t - $s"}.joinToString("\n")}"
//        composeTestRule.onNodeWithText(ingredients).assertIsDisplayed()
//        composeTestRule.onNodeWithText(steps).assertIsDisplayed()
//    }
// }
