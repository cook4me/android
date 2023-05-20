package ch.epfl.sdp.cook4me.ui.navigation

// class NavigationBarTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val navigateTo = { expectedRoute: String ->
//        { actualRoute: String ->
//            assertThat(actualRoute, `is`(expectedRoute))
//        }
//    }
//
//    private fun navigateToMainDestinationTest(destination: BottomNavScreen) {
//        composeTestRule.setContent {
//            BottomNavigationBar(
//                navigateTo = navigateTo(destination.route),
//                currentRoute = "",
//                onClickSignOut = {},
//            )
//        }
//
//        composeTestRule.onNodeWithText(destination.title).performClick()
//    }
//
//    private fun navigateToXFromDropDownMenu(destination: BottomNavScreen) {
//        composeTestRule.setContent {
//            BottomNavigationBar(
//                navigateTo = navigateTo(destination.route),
//                currentRoute = "",
//                onClickSignOut = {},
//            )
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.bottom_bar_more_button_text).performClick()
//        composeTestRule.waitUntilExists(hasText(destination.title))
//        composeTestRule.onNodeWithText(destination.title).performScrollTo()
//        composeTestRule.onNodeWithText(destination.title).performClick()
//    }

//    @Test
//    fun navigatingToEventsTest() {
//        navigateToMainDestinationTest(BottomNavScreen.Events)
//    }

//    @Test
//    fun navigatingToTupperwares() {
//        navigateToMainDestinationTest(BottomNavScreen.Tupperwares)
//    }

//    @Test
//    fun navigatingToRecipes() {
//        navigateToMainDestinationTest(BottomNavScreen.Recipes)
//    }

//    @Test
//    fun clickingOnMoreButtonShouldDisplayDropDownMenu() {
//        composeTestRule.setContent {
//            BottomNavigationBar(
//                navigateTo = navigateTo(""),
//                currentRoute = "",
//                onClickSignOut = {},
//            )
//        }
//        composeTestRule.onNodeWithStringId(R.string.bottom_bar_more_button_text).performClick()
//        composeTestRule.waitUntilExists(hasText(BottomNavScreen.Profile.title))
//    }

//    @Test
//    fun navigateToProfileFromDropDownMenu() {
//        navigateToXFromDropDownMenu(BottomNavScreen.Profile)
//    }
// }
