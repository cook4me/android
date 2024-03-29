package ch.epfl.sdp.cook4me.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import ch.epfl.sdp.cook4me.waitUntilExists
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test

class NavigationBarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navigateTo = { expectedRoute: String ->
        { actualRoute: String ->
            assertThat(actualRoute, `is`(expectedRoute))
        }
    }

    private fun navigateToMainDestinationTest(destination: BottomNavScreen) {
        composeTestRule.setContent {
            BottomNavigationBar(
                navigateTo = navigateTo(destination.route),
                currentRoute = "",
                onClickSignOut = {},
            )
        }

        composeTestRule.onNodeWithTag(destination.title).performClick()
    }

    private fun navigateToXFromDropDownMenu(destination: BottomNavScreen) {
        composeTestRule.setContent {
            BottomNavigationBar(
                navigateTo = navigateTo(destination.route),
                currentRoute = "",
                onClickSignOut = {},
            )
        }

        composeTestRule.run {
            onNodeWithStringId(R.string.bottom_bar_more_button_text).performClick()
            waitUntilExists(hasText(destination.title))
            onNodeWithText(destination.title).performScrollTo()
            onNodeWithText(destination.title).performClick()
        }
    }

    @Test
    fun navigatingToEventsTest() {
        navigateToMainDestinationTest(BottomNavScreen.Events)
    }

    @Test
    fun navigatingToTupperwares() {
        navigateToMainDestinationTest(BottomNavScreen.Tupperwares)
    }

    @Test
    fun navigatingToRecipes() {
        navigateToMainDestinationTest(BottomNavScreen.Recipes)
    }

    @Test
    fun clickingOnMoreButtonShouldDisplayDropDownMenu() {
        composeTestRule.setContent {
            BottomNavigationBar(
                navigateTo = navigateTo(""),
                currentRoute = "",
                onClickSignOut = {},
            )
        }
        composeTestRule.onNodeWithStringId(R.string.bottom_bar_more_button_text).performClick()
        composeTestRule.waitUntilExists(hasText(BottomNavScreen.Profile.title))
    }

    @Test
    fun navigateToProfileFromDropDownMenu() {
        navigateToXFromDropDownMenu(BottomNavScreen.Profile)
    }
}
